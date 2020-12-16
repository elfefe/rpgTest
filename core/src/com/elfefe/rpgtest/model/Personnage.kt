package com.elfefe.rpgtest.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.utils.clampToFarthest
import com.elfefe.rpgtest.utils.ids
import com.elfefe.rpgtest.utils.normalizeDirection
import com.elfefe.rpgtest.utils.set

open class Personnage(texturePath: String, var size: Int): Entity(texturePath) {
    override var layer = 0
    final override val physicLayer = ArrayList<Int>()
    final override val position = Vector2(0f, 0f)

    var animation: Animation<TextureRegion>
    var frames: Array<TextureRegion>

    private val lastPosition = Vector2()
    val velocity = Vector2()

    var walkspeed = 100f

    var isBlocked = false

    init {
        physicLayer.add(0)
        setSize(size.toFloat(), size.toFloat() / 2f)

        val initWizardTextureRegion = TextureRegion.split(
                texture,
                size,
                size
        )

        frames = initWizardTextureRegion[0]

        animation = Animation(0.1f, *frames)
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
                position.cpy().add(normalizeDirection),
                lastPosition,
                dir
        )

        if (!isBlocked) {
            lastPosition.set(position)
            position.set(this, pos)
        } else {
            println("${javaClass.simpleName} stuck $position last $lastPosition")
            position.set(this, lastPosition)
        }
    }

    fun currentFrame(stateTime: Float): TextureRegion? {
        return animation.getKeyFrame(stateTime, true)
    }
}