package com.rabross.noise

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import com.rabross.noise.generator.NoiseGenerator

class NoiseEngine(
    private val surfaceHolder: SurfaceHolder,
    private val pelSize: Int,
    private val noiseGenerator: NoiseGenerator
) : Noise, Renderer {

    private val renderThread = RunnableRenderThread(this)
    private val paint = Paint()
    private var width = 0
    private var height = 0

    override fun update() {}

    override fun draw() = with(surfaceHolder) {
        if (surface.isValid) {
            lockCanvas()?.run {
                draw(this)
                unlockCanvasAndPost(this)
            }
        }
    }

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

    override fun start() = renderThread.start()
    override fun stop() = renderThread.stop()
    override fun pause() = renderThread.pause()
    override fun resume() = renderThread.resume()

    override fun onSizeChanged(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    private fun randomColor(): Int {
        val color = noiseGenerator.next() and 0xff
        return Color.argb(255, color, color, color)
    }
}