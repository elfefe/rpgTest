package com.elfefe.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowState
import org.jetbrains.skiko.currentNanoTime
import java.lang.Float.max
import java.lang.Float.min

val pixelSize = Size(1f, 1f)

@Composable
fun App() {

    var octave by remember { mutableStateOf(5f) }
    var seed by remember { mutableStateOf(1f) }

    var mountains by remember { mutableStateOf(1f) }
    var progress by remember { mutableStateOf(0) }

    var width by remember { mutableStateOf(WindowState().size.width.value.toInt()) }

    val terrain = TerrainGenerator.generateTerrain(
        mapSize = width,
        octaves = octave.toInt(),
        seed = seed.toInt()
    )

    World(width, mountains, terrain) {
        width = it
    }

    Interface(octave, seed, mountains) { o, s, m ->
        octave = o
        seed = s
        mountains = m
    }
}

@Composable
fun World(width: Int, mountains: Float, terrain: MutableList<MutableList<Float>>, update: (Int) -> Unit) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {

        update(size.width.toInt())

        if (terrain.size == width)
            for (row in 0 until width) {
                for (column in 0 until width) {
                    drawRect(
                        color = color(max(0f, min(1f, (terrain[row][column] * mountains) - ((mountains - 1) / 2)))),
                        topLeft = Offset(row.toFloat(), column.toFloat()),
                        size = pixelSize
                    )
                }
            }
    }
}

@Composable
fun Interface(octave: Float, seed: Float, mountains: Float, update: (Float, Float, Float) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.4f)
    ) {
//        Text("Loading: $progress%")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Octave: $octave", color = Color.White)
            Button({
                update(octave - 1, seed, mountains)
            }, colors = ButtonDefaults.buttonColors(Color.Transparent)) {
                Text("-", fontSize = 18.sp, color = Color.White)
            }
            Button({
                update(octave + 1, seed, mountains)
            }, colors = ButtonDefaults.buttonColors(Color.Transparent)) {
                Text("+", fontSize = 18.sp, color = Color.White)
            }
            Slider(
                value = octave,
                onValueChange = {
                    update(it.toInt().toFloat(), seed, mountains)
                },
                valueRange = 0f..15f
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Seed: $seed", color = Color.White)
            Button({
                update(octave, seed - 1, mountains)
            }, colors = ButtonDefaults.buttonColors(Color.Transparent)) {
                Text("-", fontSize = 18.sp, color = Color.White)
            }
            Button({
                update(octave, seed + 1, mountains)
            }, colors = ButtonDefaults.buttonColors(Color.Transparent)) {
                Text("+", fontSize = 18.sp, color = Color.White)
            }
            Slider(
                value = seed,
                onValueChange = {
                    update(octave, it.toInt().toFloat(), mountains)
                },
                valueRange = 0f..10f
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mountains: $mountains", color = Color.White)
            Slider(
                value = mountains,
                onValueChange = {
                    update(octave, seed + 1, it)
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
