package com.rabross.noise

interface Noise {
    fun start()
    fun stop()
    fun pause()
    fun resume()
    fun onSizeChanged(width: Int, height: Int)
}