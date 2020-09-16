package com.rabross.noise

class RunnableRenderThread(private val renderer: Renderer) : RenderThread, Runnable {

    private var running = false
    private var paused = false

    override fun run() {
        while (running) {
            if (!paused) {
                renderer.update()
                renderer.draw()
            }
        }
    }

    override fun start() {
        if (!running) Thread(this).start()
        running = true
    }

    override fun resume() {
        paused = false
    }

    override fun pause() {
        paused = true
    }

    override fun stop() {
        running = false
    }

    override fun isRunning() = running

    override fun isPaused() = paused
}