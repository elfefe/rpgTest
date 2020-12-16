package com.elfefe.rpgtest.model

import com.badlogic.gdx.math.Vector2

class House(texturePath: String): Entity(texturePath) {
    override var layer = 0
    override val physicLayer = ArrayList<Int>()
    override val position = Vector2(0f, 0f)

    init {
        physicLayer.add(0)
    }
}