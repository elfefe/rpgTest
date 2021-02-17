package com.elfefe.rpgtest.utils

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import java.lang.Float.max
import java.lang.Float.min
import java.util.*
import kotlin.math.abs
import kotlin.math.max


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

// JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.

object ImprovedNoise {
    fun noise(x: Double, y: Double, z: Double): Double {
        var x1 = x
        var y1 = y
        var z1 = z
        val x2 = fastFloor(x1) and 255
        // FIND UNIT CUBE THAT
        val y2 = fastFloor(y1) and 255
        // CONTAINS POINT.
        val z2 = fastFloor(z1) and 255
        x1 -= fastFloor(x1) // FIND RELATIVE X,Y,Z
        y1 -= fastFloor(y1) // OF POINT IN CUBE.
        z1 -= fastFloor(z1)
        val u = fade(x1)
        // COMPUTE FADE CURVES
        val v = fade(y1)
        // FOR EACH OF X,Y,Z.
        val w = fade(z1)
        val a = p[x2] + y2
        val aa = p[a] + z2
        val ab = p[a + 1] + z2
        // HASH COORDINATES OF
        val b = p[x2 + 1] + y2
        val ba = p[b] + z2
        val bb = p[b + 1] + z2 // THE 8 CUBE CORNERS,
        return lerp(w, lerp(v, lerp(u, grad(p[aa], x1, y1, z1),  // AND ADD
                grad(p[ba], x1 - 1, y1, z1)),  // BLENDED
                lerp(u, grad(p[ab], x1, y1 - 1, z1),  // RESULTS
                        grad(p[bb], x1 - 1, y1 - 1, z1))),  // FROM  8
                lerp(v, lerp(u, grad(p[aa + 1], x1, y1, z1 - 1),  // CORNERS
                        grad(p[ba + 1], x1 - 1, y1, z1 - 1)),  // OF CUBE
                        lerp(u, grad(p[ab + 1], x1, y1 - 1, z1 - 1),
                                grad(p[bb + 1], x1 - 1, y1 - 1, z1 - 1))))
    }

    private fun fastFloor(x: Double): Int {
        val xi = x.toInt()
        return if (x < xi) xi - 1 else xi
    }

    private fun fade(t: Double): Double = t * t * t * (t * (t * 6 - 15) + 10)

    private fun lerp(t: Double, a: Double, b: Double): Double = a + t * (b - a)

    private fun grad(hash: Int, x: Double, y: Double, z: Double): Double {
        val h = hash and 15 // CONVERT LO 4 BITS OF HASH CODE
        val u = if (h < 8) x else y
        // INTO 12 GRADIENT DIRECTIONS.
        val v = if (h < 4) y else if (h == 12 || h == 14) x else z
        return (if (h and 1 == 0) u else -u) + if (h and 2 == 0) v else -v
    }

    private val p = IntArray(512)
    private val permutation = intArrayOf(151, 160, 137, 91, 90, 15,
            131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
            190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
            88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
            77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
            102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
            135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
            5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
            223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
            129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
            251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
            49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
            138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
    )

    init {
        for (i in 0..255) {
            p[i] = permutation[i]
            p[256 + i] = p[i]
        }
    }
}

object MapNoise {
    fun generateWhiteNoise(width: Int, height: Int): Array<Array<Float>> {
        val random = Random(0); //Seed to 0 for testing
        val noise = Array(width) { Array(height) { 0f } };

        for (i in 0 until width) {
            for (j in 0 until height) {
                noise[i][j] = (random.nextFloat() % 1) - 0.5f
            }
        }

        return noise;
    }

    fun generatePerlinNoise(width: Int, height: Int, octaveCount: Int): Array<Array<Float>> {
        val smoothNoise: Array<Array<Array<Float>>> = Array(octaveCount) {Array(0){Array(0){0f}}}

        val persistance = 0.5f

        for (i in 0 until octaveCount)
            smoothNoise[i] = generateSmoothNoise(width, height, i)

        val perlinNoise = Array(width) {Array(height){0f}}
        var amplitude = 1f
        var totalAmplitude = 0f

        for (octave in (octaveCount - 1)..0 ) {
            amplitude *= persistance
            totalAmplitude += amplitude

            for (i in 0 until width)
                for (j in 0 until height)
                    perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude
        }

        for (i in 0 until width)
            for (j in 0 until height)
                perlinNoise[i][j] /= totalAmplitude

        return perlinNoise
    }

    private fun generateSmoothNoise(width: Int, height: Int, octave: Int): Array<Array<Float>> {
        val random = Random(0) //Seed to 0 for testing
        val noise = Array(width) { Array(height) { 0f } }

        val samplePeriod = 1 shr octave
        val sampleFrequency: Float = 1f / samplePeriod

        for (i in 0 until width) {
            val sampleI0 = (i / max(samplePeriod, 1)) * samplePeriod
            val sampleI1 = (sampleI0 + samplePeriod) % width
            val horizontalBlend = (i - sampleI0) * sampleFrequency

            for (j in 0 until height) {
                val sampleJ0 = (j / max(samplePeriod, 1)) * samplePeriod
                val sampleJ1 = (sampleJ0 + samplePeriod) % height
                val verticalBlend = (j - sampleJ0) * sampleFrequency

                val top = interpolate(
                        noise[sampleI0][sampleJ0],
                        noise[sampleI1][sampleI0],
                        horizontalBlend
                )

                val bottom = interpolate(
                        noise[sampleI0][sampleJ1],
                        noise[sampleI1][sampleJ1],
                        horizontalBlend
                )

                noise[i][j] = interpolate(top, bottom, verticalBlend)
            }
        }

        return noise
    }

    private fun interpolate(x0: Float, x1: Float, alpha: Float) = x0 * (1 - alpha) + alpha * x1
}