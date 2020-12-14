package com.elfefe.rpgtest.utils

import com.badlogic.gdx.graphics.g2d.Batch
import com.elfefe.rpgtest.model.Personnage

fun Batch.draw (personnage: Personnage, stateTime: Float) {
    draw(personnage.currentFrame(stateTime), personnage.position.x, personnage.position.y)
}