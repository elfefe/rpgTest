package com.elfefe.rpgtest

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.elfefe.rpgtest.model.Personnage
import com.elfefe.rpgtest.utils.draw
import kotlin.math.abs

class RpgTest : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture

    private lateinit var blackWizard: Personnage

    private val clickPosition = Vector2()

    private var stateTime = 0f

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")

        blackWizard = Personnage(Texture("black_wizard_walking.png"))
    }

    override fun render() {
        drawBackground()
        stateTime += Gdx.graphics.deltaTime

        batch.begin()
        if (Gdx.input.isTouched) {
            clickPosition.x = Gdx.input.x.toFloat()
            clickPosition.y = (-Gdx.input.y + Gdx.graphics.height).toFloat()
        }

        blackWizard.move(clickPosition)
        batch.draw(blackWizard, stateTime)

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
        blackWizard.dispose()
    }

    private fun drawBackground() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
    }
}