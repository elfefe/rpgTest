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