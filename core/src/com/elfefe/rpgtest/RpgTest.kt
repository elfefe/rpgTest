package com.elfefe.rpgtest

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
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

    private val mouseInWorld2D = Vector2()
    private val mouseInWorld3D = Vector3()

    private lateinit var blackWizard: Personnage

    override fun create() {
        batch = SpriteBatch()

        autoTiler = AutoTiler(MAP_WIDTH, MAP_HEIGHT, Gdx.files.internal("tileset.json"))
        map = autoTiler.generateMap()

        // Setup camera
        camera = OrthographicCamera()
        viewport = FitViewport(MAP_WIDTH.toFloat(), MAP_HEIGHT.toFloat(), camera)

        // Setup map renderer
        val unitScale = 1f / max(autoTiler.tileWidth, autoTiler.tileHeight)
        renderer = OrthogonalTiledMapRenderer(map, unitScale)

        blackWizard = Personnage(Texture("black_wizard_walking.png"))
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height)

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

        if (Gdx.input.isTouched) {
            clickPosition.x = Gdx.input.x.toFloat()
            clickPosition.y = (-Gdx.input.y + Gdx.graphics.height).toFloat()
        }

        batch.begin()

        blackWizard.move(clickPosition)

        batch.draw(blackWizard, stateTime)

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