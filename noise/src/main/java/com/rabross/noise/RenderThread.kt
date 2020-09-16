package com.rabross.noise

interface RenderThread {
    fun start()
    fun resume()
    fun pause()
    fun stop()
    fun isRunning(): Boolean
    fun isPaused(): Boolean
}