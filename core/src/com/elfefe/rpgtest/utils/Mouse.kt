package com.elfefe.rpgtest.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor

object Mouse {
    val x: Int
        get() = Gdx.input.x
    val y: Int
        get() = Gdx.input.y

    var scroll: Float = 0f
        private set(value) {
            field = value
            doOnScroll(value)
        }

    var doOnScroll: (Float) -> Unit = {}

    val inputProcessor = object: InputProcessor {
        override fun keyDown(keycode: Int): Boolean = true

        override fun keyUp(keycode: Int): Boolean = true

        override fun keyTyped(character: Char): Boolean = true

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = true

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = true

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = true

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean = true

        override fun scrolled(amountX: Float, amountY: Float): Boolean {
            scroll = amountY
            return true
        }
    }

    init {
        Gdx.input.inputProcessor = inputProcessor
    }
}