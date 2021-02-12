package com.elfefe.rpgtest.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.MapManager
import com.elfefe.rpgtest.RpgTest
import com.elfefe.rpgtest.utils.*
import com.elfefe.rpgtest.utils.raycasting.LineSegment
import com.elfefe.rpgtest.utils.raycasting.RayCast
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

open class Personnage(texturePath: String, var size: Int): Entity(texturePath) {
    override var layer = PhysicLayers.ENTITY
    final override val position = Vector2(0f, 0f)
    override var interactibles: List<Interactible> = listOf()

    var animation: Animation<TextureRegion>
    var frames: Array<TextureRegion>

    private val lastPosition = Vector2()
    val velocity = Vector2()
    val ray: Vector2 = Vector2(0f, 0f)

    var walkspeed = 100f

    var isBlocked = false

    init {
        setSize(size.toFloat(), size.toFloat() / 2f)

        val initWizardTextureRegion = TextureRegion.split(
                texture,
                size,
                size
        )

        frames = initWizardTextureRegion[0]

        animation = Animation(0.1f, *frames)
    }

    override fun draw(batch: Batch) {
        move(clickPosition, MapManager.colliders())
        batch.draw(this, stateTime)
    }

    fun idle(): Boolean {
        return position == lastPosition
    }

    private fun move(direction: Vector2, segments: ArrayList<LineSegment>) {
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

        println("Normalized $normalizeDirection, Pos $pos, Direction $dir, Position $position")

        val rayPos = pos.cpy().apply {
            x += size / 2
            y += size / 2
        }
        val rays = RayCast.castRays(rayPos, 10, 12, segments)

        var intersect = Float.MAX_VALUE
        rays.forEach {
            intersect = min(rayPos.cpy().dst(it), intersect)
        }

        if (abs(intersect) > 10) {
            lastPosition.set(position)
            position.set(this, pos)
        }
    }

    fun currentFrame(stateTime: Float): TextureRegion? {
        return animation.getKeyFrame(stateTime, true)
    }
}