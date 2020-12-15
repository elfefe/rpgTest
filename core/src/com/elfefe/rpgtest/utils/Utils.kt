package com.elfefe.rpgtest.utils

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.model.Personnage
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.abs
import kotlin.math.round

fun Batch.draw (personnage: Personnage, stateTime: Float, width: Int = personnage.size, height: Int = personnage.size) {
    draw(personnage.currentFrame(stateTime), personnage.position.x, personnage.position.y, width.toFloat(), height.toFloat())
}