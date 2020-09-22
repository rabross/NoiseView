//
// Created by Robert Ross on 21/09/2020.
//

#include <jni.h>
#include <android/log.h>
#include "native-lib.h"

extern "C" JNIEXPORT void JNICALL
Java_com_rabross_noise_NoiseEngine_render(
        JNIEnv *env,
        jobject /* this */) {

    __android_log_print(ANDROID_LOG_VERBOSE, "JNI", "hello");
}
