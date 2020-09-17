package com.rabross.noise.generator

class CachingNoiseGenerator(private val noiseGenerator: NoiseGenerator, cacheSize: Int) :
    NoiseGenerator {

    private val cache: Array<Int> = Array(cacheSize) { 0 }

    private var pelCounter = 0
    private var isCacheReady = false

    override fun next(): Int {

        if (++pelCounter == cache.size - 1) {
            pelCounter = 0
            isCacheReady = true
        }

        return if (isCacheReady) {
            cache[pelCounter]
        } else {
            noiseGenerator.next().also {
                cache[pelCounter] = it
            }
        }
    }
}
