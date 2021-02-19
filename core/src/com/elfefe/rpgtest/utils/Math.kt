package com.elfefe.rpgtest.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import java.lang.Float.min
import kotlin.math.abs
import kotlin.random.Random


fun normalizeDirection(distance: Vector2): Vector2 {
    var normalizedX = if (distance.x != 0f) distance.x / abs(distance.x) else 0f
    var normalizedY = if (distance.y != 0f) distance.y / abs(distance.y) else 0f

    if (distance.x != 0f && distance.y != 0f) {
        val maxDistance = Math.max(abs(distance.y), abs(distance.x))
        if (maxDistance == abs(distance.y)) {
            normalizedX /= abs(distance.y) / abs(distance.x)
        } else if (maxDistance == abs(distance.x)) {
            normalizedY /= abs(distance.x) / abs(distance.y)
        }
    }

    return Vector2(normalizedX, normalizedY)
}

fun clampToFarthest(position: Float, lastPosition: Float, direction: Float): Float {
    return if (
//            if (direction / abs(direction) == 1f)
            min(abs(lastPosition), abs(direction)) == lastPosition
//            else max(abs(lastPosition), abs(direction)) == lastPosition
    )
        MathUtils.clamp(position, lastPosition, direction)
    else MathUtils.clamp(position, direction, lastPosition)
}

fun clampToFarthest(position: Vector2, lastPosition: Vector2, direction: Vector2): Vector2 {
    return Vector2(
            clampToFarthest(position.x, lastPosition.x, direction.x),
            clampToFarthest(position.y, lastPosition.y, direction.y)
    )
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (kotlin.math.round(this * multiplier) / multiplier).toFloat()
}

fun fastFloor(x: Double): Int {
    val xi = x.toInt()
    return if (x < xi) xi - 1 else xi
}

object MapNoise {
    fun generateWhiteNoise(width: Int, height: Int, seed: Int = 0): Array<Array<Float>> {
        val random = Random(seed) //Seed to 0 for testing
        val noise = Array(width) { Array(height) { 0f } };

        for (i in 0 until width * height) {
                val y = fastFloor(i / height / 1.0)
                val x = i - (y * height)
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
            val y = fastFloor(i / height / 1.0)
            val x = i - (y * height)
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

        for (i in 0 until octaveCount)
            smoothNoise[i] = generateSmoothNoise(generateWhiteNoise(width, height, seed), i)

        val perlinNoise = Array(width) { Array(height) { MapPixel(0f, Color.CLEAR) } }
        var amplitude = 1f
        var totalAmplitude = 0f

        for (octave in (octaveCount - 1) downTo 0) {
            amplitude *= persistance
            totalAmplitude += amplitude

            for (i in 0 until width * height) {
                val y = fastFloor(i / height / 1.0)
                val x = i - (y * height)
                perlinNoise[x][y].indice += smoothNoise[octave][x][y] * amplitude
            }
        }

        for (i in 0 until width * height) {
            val y = fastFloor(i / height / 1.0)
            val x = i - (y * height)
            perlinNoise[x][y].run {
                indice /= totalAmplitude
                color = if (indice > 0) when {
                    indice > 0.45f -> Color.WHITE
                    else -> Color.GREEN.apply {
                        g = 0.5f + (abs(indice) / 2)
                    }
                }  else Color.BLUE.apply {
                    b = 0.5f + (abs(indice) / 2)
                }
            }
        }

        return perlinNoise
    }

    private fun interpolate(x0: Float, x1: Float, alpha: Float) = x0 * (1 - alpha) + alpha * x1

    data class MapPixel(var indice: Float, var color: Color)
}