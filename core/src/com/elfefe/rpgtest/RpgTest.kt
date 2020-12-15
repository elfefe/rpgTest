package com.elfefe.rpgtest

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.elfefe.rpgtest.model.Personnage
import com.elfefe.rpgtest.utils.draw
import net.gpdev.autotile.AutoTiler
import kotlin.math.max


class RpgTest : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch

    private val clickPosition = Vector2(0f, 0f)

    private var stateTime = 0f

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var renderer: OrthogonalTiledMapRenderer
    private lateinit var autoTiler: AutoTiler
    private lateinit var map: TiledMap

    private lateinit var circle: Pixmap
    private lateinit var circleTexture: Texture

    private lateinit var mousePosition: Vector3

    private lateinit var blackWizard: Personnage

    override fun create() {
        batch = SpriteBatch()

        circle = Pixmap(8, 8, Pixmap.Format.RGBA8888)
        circle.setColor(Color.BLACK)
        circle.fillCircle(4, 4, 2)
        circleTexture = Texture(circle)

        autoTiler = AutoTiler(MAP_WIDTH, MAP_HEIGHT, Gdx.files.internal("tileset.json"))
        map = autoTiler.generateMap()

        // Setup camera
        camera = OrthographicCamera()
        viewport = FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), camera)

        // Setup map renderer
        val unitScale = 1f / max(MAP_WIDTH / Gdx.graphics.width, MAP_HEIGHT / Gdx.graphics.height)
        renderer = OrthogonalTiledMapRenderer(map, batch)

        blackWizard = Personnage(Texture("black_wizard_walking.png"), 64)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height, true)
        camera.update()

        autoTiler = AutoTiler(MAP_WIDTH, MAP_HEIGHT, Gdx.files.internal("tileset.json"))
        map = autoTiler.generateMap()
    }

    override fun render() {
        drawBackground()
        stateTime += Gdx.graphics.deltaTime

        // Render map
        viewport.apply(true)
        renderer.setView(camera)
        renderer.render()

        mousePosition = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        if (Gdx.input.isTouched) {
            clickPosition.x = mousePosition.x
            clickPosition.y = mousePosition.y
        }

        batch.begin()

        blackWizard.move(clickPosition)

        batch.draw(blackWizard, stateTime)
        batch.draw(circleTexture, clickPosition.x, clickPosition.y)

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        blackWizard.dispose()
    }

    private fun drawBackground() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
    }

    companion object {
        private const val MAP_WIDTH = 20
        private const val MAP_HEIGHT = 20
    }
}