package com.elfefe.rpgtest.model

import com.elfefe.rpgtest.utils.PhysicLayers

class Player(texturePath: String, size: Int): Personnage(texturePath, size) {
    override var layer = PhysicLayers.PLAYER
}