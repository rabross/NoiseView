//
// Created by Robert Ross on 21/09/2020.
//

#include <jni.h>
#include <android/log.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <cstdlib>

extern "C" JNIEXPORT void JNICALL
Java_com_rabross_noise_NoiseEngine_nativeRender(
        JNIEnv *env,
        jobject /* this */,
        jobject surface,
        jint pelSize) {

    ANativeWindow *window = ANativeWindow_fromSurface(env, surface);
    if (window == nullptr) {
        __android_log_print(ANDROID_LOG_FATAL, "JNI", "unable to get native window");
        return;
    }

    auto newWidth = ANativeWindow_getWidth(window) / pelSize;
    auto newHeight = ANativeWindow_getHeight(window) / pelSize;
    int32_t result = ANativeWindow_setBuffersGeometry(window, newWidth, newHeight, WINDOW_FORMAT_RGBA_8888);
    if (result < 0) {
        __android_log_print(ANDROID_LOG_FATAL, "JNI", "unable to set buffers geometry");
        ANativeWindow_release(window);
        window = nullptr;
        return;
    }
    ANativeWindow_acquire(window);

    ANativeWindow_Buffer buffer;
    if (ANativeWindow_lock(window, &buffer, nullptr) < 0) {
        __android_log_print(ANDROID_LOG_FATAL, "JNI", "unable to lock native window");
        ANativeWindow_release(window);
        window = nullptr;
        return;
    }

    auto *line = (uint32_t *) buffer.bits;
    for (int y = 0; y < buffer.height; y++) {
        for (int x = 0; x < buffer.width; x++) {
            int colorValue = rand() % 255;
            int colorABGR = (255 << 24) | (colorValue << 16) | (colorValue << 8) | colorValue;
            line[x] = colorABGR;
        }
        line += buffer.stride;
    }

    if (ANativeWindow_unlockAndPost(window) < 0) {
        __android_log_print(ANDROID_LOG_FATAL, "JNI", "unable to unlock and post to native window");
    }
    ANativeWindow_release(window);
}
