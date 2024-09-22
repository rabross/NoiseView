//
// Created by Robert Ross on 21/09/2020.
//

#include <jni.h>
#include <android/log.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <cstdlib>

extern "C" JNIEXPORT void JNICALL
Java_com_rabross_noise_renderer_NativeNoiseRenderer_nativeRender(
        JNIEnv *env,
        jobject /* this */,
        jobject surface,
        jint pelSize,
        jint style) {

    ANativeWindow *window = ANativeWindow_fromSurface(env, surface);
    if (window == nullptr) {
        __android_log_print(ANDROID_LOG_DEBUG, "JNI", "unable to get native window");
        return;
    }

    auto newWidth = ANativeWindow_getWidth(window) / pelSize;
    auto newHeight = ANativeWindow_getHeight(window) / pelSize;
    int32_t result = ANativeWindow_setBuffersGeometry(window, newWidth, newHeight, WINDOW_FORMAT_RGBA_8888);
    if (result < 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "JNI", "unable to set buffers geometry");
        ANativeWindow_release(window);
        window = nullptr;
        return;
    }
    ANativeWindow_acquire(window);

    ANativeWindow_Buffer buffer;
    if (ANativeWindow_lock(window, &buffer, nullptr) < 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "JNI", "unable to lock native window");
        ANativeWindow_release(window);
        window = nullptr;
        return;
    }

    auto *line = (uint32_t *) buffer.bits;
    for (int y = 0; y < buffer.height; y++) {
        for (int x = 0; x < buffer.width; x++) {

            if(style == 0){
                int A = 255;
                int B = rand() % 255;
                int G = rand() % 255;
                int R = rand() % 255;
                int colorABGR = (A << 24) | (B << 16) | (G << 8) | R;
                line[x] = colorABGR;
            } else {
                int colorValue = rand() % 255;
                int A = 255;
                int B = colorValue;
                int G = colorValue;
                int R = colorValue;
                int colorABGR = (A << 24) | (B << 16) | (G << 8) | R;
                line[x] = colorABGR;
            }
        }
        line += buffer.stride;
    }

    if (ANativeWindow_unlockAndPost(window) < 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "JNI", "unable to unlock and post to native window");
    }
    ANativeWindow_release(window);
}
