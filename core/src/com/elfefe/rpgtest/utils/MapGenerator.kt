package com.elfefe.rpgtest.utils

import com.elfefe.rpgtest.utils.SimplexNoise.noise
import java.util.*
import kotlin.math.floor


fun generate(width: Int, height: Int): Array<Array<Float>> {
    val array = Array(width) {Array(height) {0f} }
    for (i in 0 until width * height) {
        val y = floor(i / height.toFloat()).toInt()
        val x = i - (y * height)
        val noise = OpenSimplexNoise(1)
        array[x][y] = noise.eval(x / 100f, y / 100f)
    }
    return array
}