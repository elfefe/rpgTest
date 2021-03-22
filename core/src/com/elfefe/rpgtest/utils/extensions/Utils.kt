package com.elfefe.rpgtest.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.*
import com.elfefe.rpgtest.model.Entity
import com.elfefe.rpgtest.model.Layer
import com.elfefe.rpgtest.model.Personnage
import com.elfefe.rpgtest.utils.raycasting.LineSegment
import kotlin.math.abs

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
    entity.layer.triggerBounds.forEach { (i, rect) ->
        rect.setPosition(x + rect.offset.x, y + rect.offset.y)
    }
    entity.layer.collisionBounds.forEach { rect ->
        rect.setPosition(x + rect.offset.x, y + rect.offset.y)
    }
    set(x, y)
}

fun Vector2.set(entity: Entity, position: Vector2) {
    set(entity, position.x, position.y)
}

val Rectangle.segments: ArrayList<LineSegment>
    get() = arrayListOf(
            LineSegment(Vector2(x, y), Vector2(x + width, y)),
            LineSegment(Vector2(x + width, y), Vector2(x + width, y + height)),
            LineSegment(Vector2(x + width, y + height), Vector2(x, y + height)),
            LineSegment(Vector2(x, y + height), Vector2(x, y))
    )

val debugRenderer by lazy { ShapeRenderer() }

fun drawDebugLine(start: Vector2, end: Vector2, color: Color = Color.RED) {
    Gdx.gl.glEnable(GL20.GL_ARRAY_BUFFER_BINDING)
    debugRenderer.begin(ShapeRenderer.ShapeType.Line)
    debugRenderer.color = color
    debugRenderer.line(start, end)
    debugRenderer.end()
    Gdx.gl.glDisable(GL20.GL_BLEND)
}

fun Any.log(text: String) = println("${this.javaClass.name} - $text")