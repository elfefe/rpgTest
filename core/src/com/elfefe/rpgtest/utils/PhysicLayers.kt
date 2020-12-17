package com.elfefe.rpgtest.utils

import com.badlogic.gdx.math.Rectangle
import com.elfefe.rpgtest.model.Layer

object PhysicLayers {
    val ENTITY: Layer
        get() = Layer(0, Rectangle())
    val PLAYER:Layer
        get() = Layer(1, Rectangle())
}