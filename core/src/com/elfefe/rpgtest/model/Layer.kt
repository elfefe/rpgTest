package com.elfefe.rpgtest.model

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Shape2D


data class Layer(
        var index: Int,
        var collider: Rectangle
)