package com.elfefe.common
import java.util.Random

class BrownianMotionGenerator(val mapSize: Int) {
        private val MAX_DISPLACEMENT = 10

        fun generateTerrain(): Array<IntArray> {
            val heightMap = Array(mapSize) { IntArray(mapSize) }
            val random = Random()

            // Initialize the first point
            heightMap[0][0] = random.nextInt(MAX_DISPLACEMENT * 2) - MAX_DISPLACEMENT

            // Apply brownian motion to each point
            for (i in 0 until mapSize) {
                for (j in 0 until mapSize) {
                    val displacement = random.nextInt(MAX_DISPLACEMENT * 2) - MAX_DISPLACEMENT
                    heightMap[i][j] = (if (i > 0) heightMap[i - 1][j] else 0) + (if (j > 0) heightMap[i][j - 1] else 0) - (if (i > 0 && j > 0) heightMap[i - 1][j - 1] else 0) + displacement
                }
            }

            return heightMap
        }
}
