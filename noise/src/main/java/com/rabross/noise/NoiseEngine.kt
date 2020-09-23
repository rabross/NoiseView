package com.rabross.noise

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.Surface
import android.view.SurfaceHolder
import com.rabross.noise.generator.KotlinRandomNoiseGenerator
import com.rabross.noise.generator.NoiseGenerator

class NoiseEngine(
    private val surfaceHolder: SurfaceHolder,
    private val pelSize: Int,
    private val noiseGenerator: NoiseGenerator = KotlinRandomNoiseGenerator(),
    nativeRender: Boolean = true
) : Renderer, SurfaceHolder.Callback {

    private val renderThread = RunnableRenderThread(this)
    private val paint = Paint()
    private var width = 0
    private var height = 0

    init {
        surfaceHolder.addCallback(this)
    }

    private val render: (SurfaceHolder) -> Unit =
        if (nativeRender) ::renderNative else ::renderJVM

    override fun update() {}

    override fun draw() {
        if (surfaceHolder.surface.isValid) {
            render(surfaceHolder)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        resume()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        pause()
        holder.surface.release()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun start() = renderThread.start()

    fun stop() = renderThread.stop()

    fun pause() = renderThread.pause()

    fun resume() = renderThread.resume()

    private fun renderNative(surfaceHolder: SurfaceHolder) =
        nativeRender(surfaceHolder.surface, pelSize)

    private external fun nativeRender(surface: Surface, pelSize: Int)

    private fun renderJVM(surfaceHolder: SurfaceHolder) = with(surfaceHolder) {
        lockCanvas()?.let { canvas ->
            draw(canvas)
            unlockCanvasAndPost(canvas)
        }
    }

    private fun draw(canvas: Canvas) = with(canvas) {
        var nextYPos = 0f
        while (nextYPos <= height) {
            var nextXPos = 0f
            while (nextXPos <= width) {
                drawRect(nextXPos, nextYPos, nextXPos + pelSize, nextYPos + pelSize,
                    paint.apply { color = randomColor() })
                nextXPos += pelSize
            }
            nextYPos += pelSize
        }
    }

    private fun randomColor() = (noiseGenerator.next() and 0xff).let { colour ->
        Color.argb(255, colour, colour, colour)
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}