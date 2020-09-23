package com.rabross.noise

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.Surface
import android.view.SurfaceHolder
import com.rabross.noise.generator.NoiseGenerator

class NoiseEngine(
    private val surfaceHolder: SurfaceHolder,
    private val pelSize: Int,
    private val noiseGenerator: NoiseGenerator
) : Renderer, SurfaceHolder.Callback {

    private val renderThread = RunnableRenderThread(this)
    private val paint = Paint()
    private var width = 0
    private var height = 0

    init {
        surfaceHolder.addCallback(this)
    }

    private external fun nativeRender(surface: Surface, pelSize: Int)

    override fun update() {}

    override fun draw() = with(surfaceHolder) {
        if (surface.isValid) {
            nativeRender(surface, pelSize)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        resume()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) = pause()

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun start() = renderThread.start()

    fun stop() = renderThread.stop()

    fun pause() = renderThread.pause()

    fun resume() = renderThread.resume()

    private fun draw(canvas: Canvas) {
        var nextYPos = 0f
        while (nextYPos <= height) {
            var nextXPos = 0f
            while (nextXPos <= width) {
                canvas.drawRect(nextXPos, nextYPos, nextXPos + pelSize, nextYPos + pelSize,
                    paint.apply { color = randomColor() })
                nextXPos += pelSize
            }
            nextYPos += pelSize
        }
    }

    private fun randomColor(): Int {
        val color = noiseGenerator.next() and 0xff
        return Color.argb(255, color, color, color)
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}