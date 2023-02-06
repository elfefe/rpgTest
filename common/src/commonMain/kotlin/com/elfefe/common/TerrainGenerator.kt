package com.elfefe.common

import androidx.compose.runtime.Composable
import java.util.*
import kotlin.math.floor

object TerrainGenerator {

    const val OCTAVES = 10
    const val SEED = 0

    const val MOUNTAIN_HEIGHT = 0.8f
    const val SNOW_HEIGHT = 0.9f
    const val LAGOON_HEIGHT = 0.45f

    fun generateTerrain(
        mapSize: Int,
        octaves: Int = OCTAVES,
        seed: Int = SEED
    ): MutableList<MutableList<Float>> {
        println("Generate terrain")
        return MapNoise.generatePerlinNoise(mapSize, mapSize, octaves, seed)
    }
}