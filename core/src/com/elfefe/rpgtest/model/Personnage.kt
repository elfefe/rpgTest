package com.elfefe.rpgtest.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.RpgTest
import kotlin.math.abs

class Personnage(val texture: Texture) {
    var animation: Animation<TextureRegion>
    var frames: Array<TextureRegion>

    private val lastPosition = Vector2()
    val position = Vector2()
    val velocity = Vector2()

    var walkspeed = 1f

    init {
        val initWizardTextureRegion = TextureRegion.split(
                texture,
                BLACK_WIZARD_WALK_SIZE,
                BLACK_WIZARD_WALK_SIZE
        )

        frames = initWizardTextureRegion[0]

        animation = Animation(0.1f, *frames)
        position.x = Gdx.graphics.width / 2f - BLACK_WIZARD_WALK_SIZE / 2f
        position.y = Gdx.graphics.height / 2f - BLACK_WIZARD_WALK_SIZE / 2f
    }


    fun idle(): Boolean {
        return position == lastPosition
    }

    fun move(direction: Vector2) {
        val speed = walkspeed * Gdx.graphics.deltaTime
        val absolutePosition = position

        absolutePosition.x = (absolutePosition.x / abs(absolutePosition.x)) * speed
        absolutePosition.y = (absolutePosition.y / abs(absolutePosition.y)) * speed

        val pos = Vector2(
                MathUtils.clamp(position.x + absolutePosition.x, lastPosition.x, direction.x),
                MathUtils.clamp(position.y + absolutePosition.y, lastPosition.y, direction.y)
        )

        lastPosition.set(position)
        position.set(pos)

        if (idle()) println("Idle")
    }

    fun currentFrame(stateTime: Float): TextureRegion? {
        return animation.getKeyFrame(stateTime, true)
    }

    fun dispose() {
        texture.dispose()
    }

    companion object {
        private const val BLACK_WIZARD_WALK_SIZE = 64
    }
}