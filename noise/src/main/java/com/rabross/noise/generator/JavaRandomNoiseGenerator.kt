package com.rabross.noise.generator

class JavaRandomNoiseGenerator: NoiseGenerator {

    private val random by lazy { java.util.Random() }

    override fun next() = random.nextInt()
}