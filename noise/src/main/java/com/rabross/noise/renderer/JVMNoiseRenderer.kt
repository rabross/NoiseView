package com.rabross.noise.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import com.rabross.noise.Renderer
import com.rabross.noise.generator.NoiseGenerator

class JVMNoiseRenderer(
    private val surfaceHolder: SurfaceHolder,
    private val noiseGenerator: NoiseGenerator,
    private val pelSize: Int,
    private val style: Int
) : Renderer {

    override fun update() {}

    override fun draw(): Unit = with(surfaceHolder) {
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
                drawRect(nextXPos, nextYPos, nextXPos + pelSize, nextYPos + pelSize,  Paint().apply { color = randomColor() })
                nextXPos += pelSize
            }
            nextYPos += pelSize
        }
    }

    private fun randomColor(): Int {

        return when(style){
            0 -> {
                Color.argb(255, noiseGenerator.next(), noiseGenerator.next(), noiseGenerator.next())
            }
            else -> {
                val color = noiseGenerator.next()
                Color.argb(255, color, color, color)
            }
        }
    }
}