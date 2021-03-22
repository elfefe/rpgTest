package com.elfefe.rpgtest.utils.noise

import com.badlogic.gdx.graphics.Color
import com.elfefe.rpgtest.utils.SAND
import com.elfefe.rpgtest.utils.fastFloor
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.random.Random


object MapNoise {
    var ocean = Color.BLUE
    private set
    var forest = Color.GREEN
        private set
    var ice = Color.CYAN
        private set

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

    fun generatePerlinNoise(width: Int, height: Int, octaveCount: Int, seed: Int = 0): Array<Array<MapPixel>> {
        val smoothNoise: Array<Array<Array<Float>>> = Array(octaveCount) { Array(0) { Array(0) { 0f } } }

        val persistance = 0.5f

        println("Generating map")
        for (i in 0 until octaveCount)
            smoothNoise[i] = generateSmoothNoise(generateWhiteNoise(width, height, seed), i)

        val perlinNoise = Array(width) { Array(height) { MapPixel(0f, Color.CLEAR) } }
        var amplitude = 1f
        var totalAmplitude = 0f

        println("Calculating mountains")
        for (octave in (octaveCount - 1) downTo 0) {
            amplitude *= persistance
            totalAmplitude += amplitude

            for (i in 0 until width * height) {
                val x = fastFloor(i / height / 1.0)
                val y = i - (x * height)
                perlinNoise[x][y].indice += smoothNoise[octave][x][y] * amplitude
            }
        }

        println("Calculating beaches")
//        val sendNoise = generateSmoothNoise(generateWhiteNoise(width, height, seed), 7)


        println("Calculating regions")
        for (i in 0 until width * height) {
            val x = ceil(i / height / 1f).toInt()
            val y = i - (x * height)
            val percent = 100f - ((y * 1F / height) * 100f)
            perlinNoise[x][y].run {
                indice /= totalAmplitude
                mappingColors(percent, 1f)
            }
        }

        return perlinNoise
    }

    private fun MapPixel.mappingColors(percent: Float, sand: Float) {
        ocean = Color(0f, 0.2f + indice, 0.63f, 1f)
        forest = Color(0f, 0.75f - indice / 2, 0f, 1f)
        ice = Color(0f, 0.78f, 1f, 1f)
        val snow = 0.3f * log10(percent) - 0.1f
        if (indice > snow || snow.isNaN()) {
            biome = Biome.ICE
            color = Color.WHITE
        } else if (indice > 0.1f) {
            when {
                indice < 0.11f && sand > 0f -> {
                    biome = Biome.SAND
                    color = SAND
                }
                else -> {
                    biome = Biome.FOREST
                    color = forest
                }
            }
        } else {
            if (indice > snow - 0.05f) {
                biome = Biome.ICE
                color = ice
            } else {
                biome = Biome.OCEAN
                color = ocean
            }
        }
    }

    private fun interpolate(x0: Float, x1: Float, alpha: Float) = x0 * (1 - alpha) + alpha * x1

    data class MapPixel(var indice: Float, var color: Color, var biome: Biome = Biome.NOWHERE)

    enum class Biome {
        OCEAN,
        FOREST,
        SAND,
        ICE,
        NOWHERE
    }
}