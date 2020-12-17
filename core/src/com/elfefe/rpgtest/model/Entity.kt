package com.elfefe.rpgtest.model

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.RpgTest
import com.elfefe.rpgtest.utils.ids
import com.elfefe.rpgtest.utils.segments

abstract class Entity(texturePath: String): Sprite(Texture(texturePath)) {
    abstract var layer: Int
    abstract val physicLayer: ArrayList<Int>
    abstract val position: Vector2

    val id: Int = ids

    fun collider(): Rectangle = boundingRectangle
    fun dispose() = texture.dispose()

    init {
        RpgTest.colliders.addAll(collider().segments)
    }
}