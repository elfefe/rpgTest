package com.elfefe.common

import androidx.compose.ui.graphics.Color

val Color.Companion.MOUNTAIN: Color
    get() = Color(0xff9f8d8d)
val Color.Companion.GRASS: Color
    get() = Color(0xff1fce1d)
val Color.Companion.FOREST: Color
    get() = Color(0xff18A117)
val Color.Companion.SAND: Color
    get() = Color(0xffFDF398)
val Color.Companion.LAGOON: Color
    get() = Color(0xff0CD8E9)
val Color.Companion.SEA: Color
    get() = Color(0xff0086FF)
val Color.Companion.DEEP_SEA: Color
    get() = Color(0xff091c66)

fun Color.interpolate(color: Color, weight: Float): Color {
    val reversedWeight = 1.0f - weight
    val red = red * reversedWeight + color.red * weight
    val green = green * reversedWeight + color.green * weight
    val blue = blue * reversedWeight + color.blue * weight
    return Color(red, green, blue)
}