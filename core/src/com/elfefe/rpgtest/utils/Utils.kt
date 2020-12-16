package com.elfefe.rpgtest.utils

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.model.Entity
import com.elfefe.rpgtest.model.Personnage
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.abs
import kotlin.math.round

fun Batch.draw (personnage: Personnage, stateTime: Float, width: Int = personnage.size, height: Int = personnage.size) {
    draw(personnage.currentFrame(stateTime), personnage.position.x, personnage.position.y, width.toFloat(), height.toFloat())
}

fun Batch.draw (entity: Entity, width: Int = entity.width.toInt(), height: Int = entity.height.toInt()) {
    draw(entity.texture, entity.position.x, entity.position.y, width.toFloat(), height.toFloat())
}

fun ArrayList<Entity>.addEntities(vararg entity: Entity) {
    entity.forEach {
        add(it)
    }
}

fun Vector2.set(entity: Entity, x: Float, y: Float) {
    entity.setPosition(x, y)
    set(x, y)
}

fun Vector2.set(entity: Entity, position: Vector2) {
    entity.setPosition(position.x, position.y)
    set(position.x, position.y)
}