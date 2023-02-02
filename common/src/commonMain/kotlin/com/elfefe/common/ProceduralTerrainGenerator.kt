package com.elfefe.common

import androidx.compose.ui.geometry.Size
import java.util.*
import kotlin.math.floor

class ProceduralTerrainGenerator(size: Size, height: Int = 255) {
    private val mapWidth = floor(size.width).toInt()
    private val mapHeight = floor(size.height).toInt()
    private val maxHeight = height
    fun generateTerrain(): Array<IntArray> {
        val heightMap = Array(mapWidth) { IntArray(mapWidth) }
        val random = Random()

        // Initialize the corners of the map
        heightMap[0][0] = random.nextInt(maxHeight)
        heightMap[0][mapWidth - 1] = random.nextInt(maxHeight)
        heightMap[mapWidth - 1][0] = random.nextInt(maxHeight)
        heightMap[mapWidth - 1][mapWidth - 1] = random.nextInt(maxHeight)

        // Recursively divide the map and add random height values
        divideTerrain(heightMap, 0, 0, mapWidth - 1, mapWidth - 1, random)

        return heightMap
    }

    private fun divideTerrain(heightMap: Array<IntArray>, x1: Int, y1: Int, x2: Int, y2: Int, random: Random) {
        if (x2 - x1 < 2 || y2 - y1 < 2) {
            return
        }
        val midX = (x1 + x2) / 2
        val midY = (y1 + y2) / 2
        heightMap[midX][midY] =
            (heightMap[x1][y1] + heightMap[x1][y2] + heightMap[x2][y1] + heightMap[x2][y2]) / 4 + random.nextInt(
                maxHeight / 2
            ) - maxHeight / 4
        heightMap[midX][y1] =
            (heightMap[x1][y1] + heightMap[x2][y1]) / 2 + random.nextInt(maxHeight / 2) - maxHeight / 4
        heightMap[midX][y2] =
            (heightMap[x1][y2] + heightMap[x2][y2]) / 2 + random.nextInt(maxHeight / 2) - maxHeight / 4
        heightMap[x1][midY] =
            (heightMap[x1][y1] + heightMap[x1][y2]) / 2 + random.nextInt(maxHeight / 2) - maxHeight / 4
        heightMap[x2][midY] =
            (heightMap[x2][y1] + heightMap[x2][y2]) / 2 + random.nextInt(maxHeight / 2) - maxHeight / 4
        divideTerrain(heightMap, x1, y1, midX, midY, random)
        divideTerrain(heightMap, x1, midY, midX, y2, random)
        divideTerrain(heightMap, midX, y1, x2, midY, random)
        divideTerrain(heightMap, midX, midY, x2, y2, random)
    }
}