package com.rabross.noise.generator

import kotlin.random.Random

class KotlinRandomNoiseGenerator : NoiseGenerator {
    override fun next() = Random.nextInt()
}