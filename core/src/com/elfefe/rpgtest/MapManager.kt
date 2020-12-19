package com.elfefe.rpgtest

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.elfefe.rpgtest.model.Entity
import com.elfefe.rpgtest.model.House
import com.elfefe.rpgtest.model.Player
import com.elfefe.rpgtest.utils.*
import com.elfefe.rpgtest.utils.raycasting.LineSegment
import net.gpdev.autotile.AutoTiler
import java.util.ArrayList
import kotlin.math.max

class MapManager {
    private var batch: SpriteBatch = SpriteBatch()

    private var viewport: Viewport
    private var renderer: OrthogonalTiledMapRenderer
    private var autoTiler: AutoTiler
    private var map: TiledMap

    private var circle: Pixmap = Pixmap(8, 8, Pixmap.Format.RGBA8888)
    private var circleTexture: Texture

    private lateinit var mousePosition: Vector3

    private val player: Player
    private val house: House

    init {
        circle.setColor(Color.BLACK)
        circle.fillCircle(4, 4, 2)
        circleTexture = Texture(circle)

        autoTiler = AutoTiler(MAP_WIDTH, MAP_HEIGHT, Gdx.files.internal("tileset.json"))
        map = autoTiler.generateMap()

        // Setup camera
        viewport = FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), camera)

        // Setup map renderer
        val unitScale = 1f / max(MAP_WIDTH / Gdx.graphics.width, MAP_HEIGHT / Gdx.graphics.height)
        renderer = OrthogonalTiledMapRenderer(map, batch)

        player = Player("black_wizard_walking.png", 64)
        house = House("house_1.png")

        entities.addEntities(
                player,
                house
        )
    }

    fun recalculate(width: Int, height: Int) {
        viewport.update(width, height, true)
        camera.update()

        autoTiler = AutoTiler(MAP_WIDTH, MAP_HEIGHT, Gdx.files.internal("tileset.json"))
        map = autoTiler.generateMap()
    }

    fun render() {
        drawBackground()

        stateTime += Gdx.graphics.deltaTime

        // Render map
        viewport.apply(true)
        renderer.setView(camera)
        renderer.render()

        checkMouseEvent()

        checkCollisions()

        batch.begin()
        orderEntities()
        batch.end()

        entities.forEach { entity ->
            entity.layer.triggerBounds.forEach { (_, rect) ->
                rect.segments.forEach { lineSegment ->
                    drawDebugLine(lineSegment.A, lineSegment.B, Color.GREEN)
                }
            }
            entity.layer.collisionBounds.forEach { rect ->
                rect.segments.forEach { lineSegment ->
                    drawDebugLine(lineSegment.A, lineSegment.B, Color.YELLOW)
                }
            }
        }

        player.collider.segments.forEach {
            drawDebugLine(it.A, it.B, Color.WHITE)
        }
    }

    private fun checkCollisions() {
        entities.forEach { entity ->
            if (entity.id != player.id) {
                var isTouching = false
                player.layer.physic.forEach { physic ->
                    if (entity.layer.physic.contains(physic) && player.collider.overlaps(entity.collider)) {
                        isTouching = true
                    }
                }
                player.isBlocked = isTouching
            }
        }
    }

    fun dispose() {
        batch.dispose()
        player.dispose()
    }

    private fun drawBackground() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
    }

    private fun orderEntities() {
        entities.forEach { entity ->
            entity.layer.triggerBounds.forEach { (order, rect) ->
                if (player.boundingRectangle.overlaps(rect)) {
                    entity.layer.order = order
                }
            }
        }
        entities.sortBy { it.layer.order }
        var order = 0
        entities.forEach {
            it.draw(batch)
        }
    }

    private fun checkMouseEvent() {

        mousePosition = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))

        if (Gdx.input.isTouched) {
            clickPosition.x = mousePosition.x
            clickPosition.y = mousePosition.y
        }
    }

    companion object {
        private const val MAP_WIDTH = 20
        private const val MAP_HEIGHT = 20

        val camera = OrthographicCamera()

        fun colliders(): ArrayList<LineSegment> {
            val lineSegments = arrayListOf<LineSegment>()
            entities.forEach {
                PhysicLayers.PLAYER.physic.forEach { physic ->
                    if (!it.layer.physic.contains(physic))
                        it.layer.collisionBounds.forEach {
                            lineSegments.addAll(it.segments)
                        }
                }
            }
            return lineSegments
        }
    }
}