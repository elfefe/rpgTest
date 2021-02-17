package com.elfefe.rpgtest

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.jnigen.NativeCodeGenerator


class RpgTest : ApplicationAdapter() {
    lateinit var game: GameManager

    override fun create() {
        game = GameManager()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        game.recalculate(width, height)
    }

    override fun render() {
        game.render()
    }

    override fun dispose() {
        super.dispose()
        game.dispose()
    }
}