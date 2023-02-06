package com.elfefe.common

import androidx.compose.ui.graphics.Color
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.random.Random


object MapNoise {
    fun generateWhiteNoise(width: Int, height: Int, seed: Int = 0): Array<Array<Float>> {
        val random = Random(seed) //Seed to 0 for testing
        val noise = Array(width) { Array(height) { 0f } };

        for (i in 0 until width * height) {
            val x = fastFloor(i / height / 1.0)
            val y = i - (x * height)

            noise[x][y] = (random.nextFloat() % 1) - 0.5f
        }

        return noise;
    }

    fun generateSmoothNoise(baseNoise: Array<Array<Float>>, octave: Int): Array<Array<Float>> {
        val width: Int = baseNoise.size
        val height: Int = baseNoise[0].size

        val noise = Array(width) { Array(height) { 0f } }

        val samplePeriod: Int = 1 shl octave
        val sampleFrequency: Float = 1f / samplePeriod

        for (i in 0 until width * height) {
            val x = fastFloor(i / height / 1.0)
            val y = i - (x * height)
            // Calculate the horizontal sampling indices
            val sampleX0: Int = (x / samplePeriod) * samplePeriod
            val sampleX1: Int = (sampleX0 + samplePeriod) % width
            val horizontalBlend: Float = (x - sampleX0) * sampleFrequency

            // Calculate the vertical sampling indices
            val sampleY0: Int = (y / samplePeriod) * samplePeriod
            val sampleY1: Int = (sampleY0 + samplePeriod) % height
            val verticalBlend: Float = (y - sampleY0) * sampleFrequency

            // Blend the top two corners
            val top = interpolate(
                    baseNoise[sampleX0][sampleY0],
                    baseNoise[sampleX1][sampleY0],
                    horizontalBlend
            )

            // Blend the bottom two corners
            val bottom = interpolate(
                    baseNoise[sampleX0][sampleY1],
                    baseNoise[sampleX1][sampleY1],
                    horizontalBlend
            )

            // Final blend
            noise[x][y] = interpolate(top, bottom, verticalBlend)
        }

        return noise
    }

    fun generateNoise(width: Int, height: Int, octave: Int, seed: Int): Array<Array<Float>> {
        return generateSmoothNoise(generateWhiteNoise(width, height, seed), 1)
    }

    fun generatePerlinNoise(width: Int, height: Int, octaveCount: Int, seed: Int = 0): MutableList<MutableList<Float>> {
        val smoothNoise: Array<Array<Array<Float>>> = Array(octaveCount) { Array(0) { Array(0) { 0f } } }

        val persistance = 0.5f

        println("Generating map")
        for (i in 0 until octaveCount)
            smoothNoise[i] = generateSmoothNoise(generateWhiteNoise(width, height, seed), i)

        val perlinNoise = MutableList(width) { MutableList(height) { 0f } }
        var amplitude = 1f
        var totalAmplitude = 0f

        println("Calculating mountains")
        for (octave in (octaveCount - 1) downTo 0) {
            amplitude *= persistance
            totalAmplitude += amplitude

            for (i in 0 until width * height) {
                val x = fastFloor(i / height / 1.0)
                val y = i - (x * height)
                perlinNoise[x][y] += smoothNoise[octave][x][y] * amplitude
            }
        }

        println("Calculating regions")
        for (i in 0 until width * height) {
            val x = ceil(i / height / 1f).toInt()
            val y = i - (x * height)
            perlinNoise[x][y] /= totalAmplitude
            perlinNoise[x][y] += 0.5f
        }

        return perlinNoise
    }

    private fun MapPixel.mappingColors(percent: Float, sand: Float) {
        val ocean = Color(0f, 0f, 0.63f, 1f)
        val forest = Color(0f, 0.75f - indice / 2, 0f, 1f)
        val glace = Color(0f, 0.78f, 1f, 1f)
        val neige = 0.3f * log10(percent) - 0.1f
        color =
                if (indice > neige || neige.isNaN()) Color.White
                else if (indice > 0.1f) {
                    when {
                        indice < 0.11f && sand > 0f -> Color.Yellow
                        else -> forest
                    }
                } else {
                    if (indice > neige - 0.05f) glace
                    else ocean
                }
    }

    private fun interpolate(x0: Float, x1: Float, alpha: Float) = x0 * (1 - alpha) + alpha * x1

    data class MapPixel(var indice: Float, var color: Color)
}