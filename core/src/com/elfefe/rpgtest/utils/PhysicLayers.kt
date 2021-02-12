package com.elfefe.rpgtest.utils

import com.badlogic.gdx.math.Rectangle
import com.elfefe.rpgtest.model.Layer

object PhysicLayers {
    /*** The physic layer is composed of a map containing
     * the index of the layer, the order of the layer and
     * the bounds of the layer ***/
    val ENTITY: Layer
        get() = Layer(2, arrayListOf(0), hashMapOf(), arrayListOf())
    val PLAYER:Layer
        get() = Layer(10, arrayListOf(1), hashMapOf(), arrayListOf())
}