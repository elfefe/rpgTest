package com.elfefe.rpgtest.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.utils.PhysicLayers
import com.elfefe.rpgtest.utils.draw
import com.elfefe.rpgtest.utils.set

class House(texturePath: String): Entity(texturePath) {
    override var layer = PhysicLayers.ENTITY
    override val position = Vector2(0f, 0f)

    init {
        layer.triggerBounds[0] = Rectangle(Vector2(0f, 0f), position.x, position.y, width, height * 0.4f)
        layer.triggerBounds[2] = Rectangle(Vector2(0f, height * 0.4f), position.x, position.y, width, height * 0.5f)

        layer.collisionBounds.add(Rectangle(Vector2(0f,(height * 0.1f) / 2), position.x, position.y, width * 1.04f, height * 0.4f))
    }

    override fun draw(batch: Batch?) {
        position.set(this, Gdx.graphics.width / 4f, Gdx.graphics.height / 4f)
        batch?.draw(this)
    }
}