package com.rabross.noise.generator

import kotlin.random.Random

class KotlinRandom : NoiseGenerator {
    override fun next() = Random.nextInt()
}