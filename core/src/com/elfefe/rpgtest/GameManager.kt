package com.elfefe.rpgtest

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.elfefe.rpgtest.model.House
import com.elfefe.rpgtest.model.Player
import com.elfefe.rpgtest.utils.*
import com.elfefe.rpgtest.utils.raycasting.LineSegment
import net.gpdev.autotile.AutoTiler
import java.util.ArrayList

class GameManager {
    private var batch: SpriteBatch = SpriteBatch()

    val camera = OrthographicCamera()
    private var viewport: Viewport
    private var renderer: OrthogonalTiledMapRenderer
    private var autoTiler: AutoTiler
    private var map: TiledMap

    private var circle: Pixmap = Pixmap(8, 8, Pixmap.Format.RGBA8888)
    private var circleTexture: Texture

    private val mapGenerator = MapGenerator()

    private lateinit var mousePosition: Vector3
    private val cameraPosition = Vector2()

    private val player: Player
    private val house: House

    init {
        circle.setColor(Color.BLACK)
        circle.fillCircle(4, 4, 2)
        circleTexture = Texture(circle)

        // Setup camera
        viewport = FitViewport(
            Gdx.graphics.width.toFloat() * ZOOM_CAMERA,
            Gdx.graphics.height.toFloat() * ZOOM_CAMERA, camera
        )

        Mouse.doOnScroll = {
            viewport.setWorldSize(
                viewport.worldWidth + Mouse.scroll * ZOOM_SPEED,
                viewport.worldHeight + Mouse.scroll * ZOOM_SPEED
            )
        }

        // Generate world map
        autoTiler = AutoTiler(Gdx.graphics.width, Gdx.graphics.height, Gdx.files.internal("tileset.json"))
        map = autoTiler.generateMap(mapGenerator.generated)


        // Setup map renderer
//        val unitScale = 1f / max(MAP_WIDTH / Gdx.graphics.width, MAP_HEIGHT / Gdx.graphics.height)
        renderer = OrthogonalTiledMapRenderer(map, batch)

        player = Player("black_wizard_walking.png", 64)
        house = House("house_1.png")

        entities.addEntities(
                player,
                house
        )
    }

    fun recalculate(width: Int, height: Int) {
        viewport.update(width, height, false)
        camera.update()

//        autoTiler = AutoTiler(MAP_WIDTH, MAP_HEIGHT, Gdx.files.internal("tileset.json"))
//        map = autoTiler.generateMap()
    }

    fun render() {
        drawBackground()

        stateTime += Gdx.graphics.deltaTime

        // Render map
        cameraPosition.set(Keys.direction(CAMERA_MOVEMENT_SPEED))
        camera.translate(cameraPosition)
        viewport.apply(false)
        renderer.setView(camera)
        renderer.render()

//        mapGenerator.draw()

        checkMouseEvent()

        checkCollisions()

        batch.begin()
        orderEntities()
        batch.end()

        entities.forEach { entity ->
            entity.layer.triggerBounds.forEach { (_, rect) ->
                rect.segments.forEach { lineSegment ->
//                    drawDebugLine(lineSegment.A, lineSegment.B, Color.GREEN)
                }
            }
            entity.layer.collisionBounds.forEach { rect ->
                rect.segments.forEach { lineSegment ->
//                    drawDebugLine(lineSegment.A, lineSegment.B, Color.YELLOW)
                }
            }
        }

        player.collider.segments.forEach {
//            drawDebugLine(it.A, it.B, Color.WHITE)
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
                entity.interactibles.forEach {
                    if (player.collider.overlaps(entity.layer.triggerBounds[it.id]) && it.asInteracted) {
                        it.interaction()
                    }
                }
                player.isBlocked = isTouching
            }
        }
    }

    fun dispose() {
        batch.dispose()
        player.dispose()
        mapGenerator.dispose()
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

        entities.forEach {
//            TODO: Remove to show entities
            it.draw(batch)
        }
    }

    private fun checkMouseEvent() {

        mousePosition = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))

        if (Gdx.input.isTouched) {
            clickPosition.x = mousePosition.x
            clickPosition.y = mousePosition.y

            entities.forEach {
                it.interact(clickPosition)
            }
        }
    }

    companion object {
        private val MAP_WIDTH = GameConfig.config.get(GameConfig.MAP).get(GameConfig.WIDTH).asInt()
        private val MAP_HEIGHT = GameConfig.config.get(GameConfig.MAP).get(GameConfig.HEIGHT).asInt()
        private val CAMERA_WIDTH = GameConfig.config.get(GameConfig.CAMERA).get(GameConfig.WIDTH).asInt()
        private val CAMERA_HEIGHT = GameConfig.config.get(GameConfig.CAMERA).get(GameConfig.HEIGHT).asInt()

        const val CAMERA_MOVEMENT_SPEED = 20

        const val ZOOM_CAMERA = 3f
        const val ZOOM_SPEED = 100f

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