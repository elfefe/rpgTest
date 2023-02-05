package com.elfefe.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.WindowState
import org.jetbrains.skiko.currentNanoTime
import java.lang.Float.max
import java.lang.Float.min

@Composable
fun App(window: ComposeWindow) {
    val pixelSize = Size(1f, 1f)

    var octave by remember { mutableStateOf(8f) }
    var seed by remember { mutableStateOf(1f) }

    var mountains by remember { mutableStateOf(1f) }
    var progress by remember { mutableStateOf(0) }

    val time = currentNanoTime()

    var width by remember { mutableStateOf(WindowState().size.width.value.toInt()) }

    val cityGenerator = CityGenerator()
    cityGenerator.generateCity()

    val terrain = TerrainGenerator.generateTerrain(
        mapSize = width,
        octaves = octave.toInt(),
        seed = seed.toInt()
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {

//        width = size.width.toInt()

        for (row in 0 until width) {
            for (column in 0 until width) {
                drawRect(
                    color = color(max(0f, min(1f, (terrain[row][column] * mountains) - ((mountains - 1) / 2)))),
                    topLeft = Offset(row.toFloat(), column.toFloat()),
                    size = pixelSize
                )
            }
        }

        println(currentNanoTime() - time)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.4f)
    ) {
//        Text("Loading: $progress%")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Octave: $octave", color = Color.White)
            Slider(
                value = octave,
                onValueChange = {
                    octave = it.toInt().toFloat()
                },
                valueRange = 0f..15f
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Seed: $seed", color = Color.White)
            Slider(
                value = seed,
                onValueChange = {
                    seed = it.toInt().toFloat()
                },
                valueRange = 0f..10f
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mountains: $mountains", color = Color.White)
            Slider(
                value = mountains,
                onValueChange = {
                    mountains = it
                },
                valueRange = 1f..2f
            )
        }
    }
}

fun color(height: Float): Color {
    var water = Color.DEEP_SEA.interpolate(Color.SEA, height)
    if (height > TerrainGenerator.LAGOON_HEIGHT)
        water = water.interpolate(Color.LAGOON, height)
    var land = Color.GRASS.interpolate(Color.FOREST, height.normalizeTo(.5f))
    if (height > TerrainGenerator.MOUNTAIN_HEIGHT)
        land = land.interpolate(
            Color.MOUNTAIN,
            height.normalizeTo(TerrainGenerator.MOUNTAIN_HEIGHT)
        )
    if (height > TerrainGenerator.SNOW_HEIGHT)
        land = land.interpolate(
            Color.White,
            height.normalizeTo(TerrainGenerator.SNOW_HEIGHT)
        )
    return if (height > 0.5f) land else water
}
