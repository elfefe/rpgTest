package com.elfefe.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import org.jetbrains.skiko.currentNanoTime

@Composable
fun App() {
    var octave by remember { mutableStateOf(10f) }
    var seed by remember { mutableStateOf(1f) }
    var gain by remember { mutableStateOf(1f) }

    var progress by remember { mutableStateOf(0) }

    val time = currentNanoTime()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        try {
            val terrain = TerrainGenerator(
                mapSize = size.width.toInt(),
                octaves = octave.toInt()
            ).generateTerrain {
                progress = it
            }

            val pixelSize = Size(1f, 1f)

            for (row in 0 until size.width.toInt()) {
                for (column in 0 until size.width.toInt()) {
                    drawRect(
                        color = terrain[row][column].color,
                        topLeft = Offset(row.toFloat(), column.toFloat()),
                        size = pixelSize
                    )
                }
            }
        } catch (e: Throwable) {
            println(e)
        }

        println(currentNanoTime() - time)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .background(Color(0x55ffffff))
    ) {
        Text("Loading: $progress%")
        Row {
            Text("Octave: $octave")
            Slider(
                value = octave,
                onValueChange = {
                    octave = it
                },
                valueRange = 0f..30f
            )
        }
        Row {
            Text("Seed: $seed")
            Slider(
                value = seed,
                onValueChange = {
                    seed = it
                },
                valueRange = 0f..1f
            )
        }
    }
}
