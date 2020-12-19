package com.elfefe.rpgtest.model

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Rectangle(
        var offset: Vector2 = Vector2.Zero.cpy(),
        x: Float = 0f,
        y: Float = 0f,
        width: Float = 0f,
        height: Float = 0f
): Rectangle(x, y, width, height)