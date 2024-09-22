package com.rabross.noise.renderer

import android.view.Surface
import android.view.SurfaceHolder
import com.rabross.noise.Renderer

class NativeNoiseRenderer(
    private val surfaceHolder: SurfaceHolder,
    private val pelSize: Int,
    private val style: Int
) : Renderer {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun update() {}

    override fun draw() {
        nativeRender(surfaceHolder.surface, pelSize, style)
    }

    private external fun nativeRender(surface: Surface, pelSize: Int, style: Int)
}