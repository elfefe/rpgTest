package com.elfefe.rpgtest.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.utils.clampToFarthest
import com.elfefe.rpgtest.utils.normalizeDirection

class Personnage(private val texture: Texture, var size: Int) {
    var animation: Animation<TextureRegion>
    var frames: Array<TextureRegion>

    private val lastPosition = Vector2()
    val position = Vector2()
    val velocity = Vector2()

    var walkspeed = 100f

    init {
        val initWizardTextureRegion = TextureRegion.split(
                texture,
                size,
                size
        )

        frames = initWizardTextureRegion[0]

        animation = Animation(0.1f, *frames)
        position.x = Gdx.graphics.width / 2f - size / 2f
        position.y = Gdx.graphics.height / 2f - size / 2f
    }


    fun idle(): Boolean {
        return position == lastPosition
    }

    fun move(direction: Vector2) {
        val dir = direction.cpy().apply {
            x -= size / 2
            y -= size / 2
        }
        val speed = walkspeed * Gdx.graphics.deltaTime
        val distance = dir.cpy().sub(position)
        val normalizeDirection = normalizeDirection(distance).scl(speed)
        val pos = clampToFarthest(
                position.add(normalizeDirection),
                lastPosition,
                dir
        )


        lastPosition.set(position)
        position.set(pos)
    }

    fun currentFrame(stateTime: Float): TextureRegion? {
        return animation.getKeyFrame(stateTime, true)
    }

    fun dispose() {
        texture.dispose()
    }
}