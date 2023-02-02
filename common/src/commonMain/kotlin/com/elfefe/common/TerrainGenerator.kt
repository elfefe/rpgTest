package com.elfefe.common

import java.util.*
import kotlin.math.floor

class TerrainGenerator(
    private val mapSize: Int,
    private val octaves: Int = OCTAVES,
    private val seed: Int = SEED
) {

    fun generateTerrain(progress: (Int) -> Unit = {}): Array<Array<MapNoise.MapPixel>> =
        MapNoise.generatePerlinNoise(mapSize, mapSize, octaves, seed)

    companion object {
        private const val OCTAVES = 10
        private const val SEED = 0
    }
}