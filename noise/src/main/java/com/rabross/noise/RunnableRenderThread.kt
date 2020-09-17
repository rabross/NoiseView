package com.rabross.noise

class RunnableRenderThread(private val renderer: Renderer) : RenderThread, Runnable {

    private val renderThread = Thread(this)
    private var running = false
    private var paused = true

    override fun run() {
        while (running) {
            if (!paused) {
                renderer.update()
                renderer.draw()
            }
        }
    }

    override fun start() {
        if (!isRunning()) renderThread.start()
        running = true
    }

    override fun resume() {
        paused = false
    }

    override fun pause() {
        paused = true
    }

    override fun stop() {
        if (isRunning()) {
            running = false
            try {
                renderThread.join()
            } catch (e: InterruptedException) {
            }
        }
    }

    override fun isRunning() = running

    override fun isPaused() = paused
}