package com.elfefe.rpgtest.model

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.RpgTest
import com.elfefe.rpgtest.utils.ids
import com.elfefe.rpgtest.utils.segments
import javax.swing.text.Position

abstract class Entity(texturePath: String): Sprite(Texture(texturePath)) {
    abstract var layer: Layer
    abstract val position: Vector2

    val id: Int = ids
    val collider: Rectangle
        get() = boundingRectangle

    fun dispose() = texture.dispose()
}