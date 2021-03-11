package com.elfefe.rpgtest.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2

object Keys {
    var top = Input.Keys.UP
    var bottom = Input.Keys.DOWN
    var left = Input.Keys.LEFT
    var right = Input.Keys.RIGHT

    fun direction(speed: Int) : Vector2 {
        val direction = Vector2()
        var key: Int
        if (Gdx.input.isKeyPressed(left.also { key = it })
        || Gdx.input.isKeyPressed(right.also { key = it }))
            direction.x = key(key) * speed
        if (Gdx.input.isKeyPressed(top.also { key = it })
        || Gdx.input.isKeyPressed(bottom.also { key = it }))
            direction.y = key(key) * speed
        return direction
    }

    private fun key(key: Int) = when (key) {
        left, bottom -> -1f
        right, top -> 1f
        else -> 0f
    }
}