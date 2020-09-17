package com.rabross.noise.generator

class JavaRandom: NoiseGenerator {

    private val random by lazy { java.util.Random() }

    override fun next() = random.nextInt()
}