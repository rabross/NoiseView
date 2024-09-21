package com.rabross.noise

import android.view.SurfaceHolder

class SurfaceHolderAdapterRenderer(
    private val surfaceHolder: SurfaceHolder,
    private val renderer: Renderer,
) : Renderer {

    override fun update() {
        renderer.update()
    }

    override fun draw() {
        if (surfaceHolder.surface.isValid) {
            renderer.draw()
        }
    }
}