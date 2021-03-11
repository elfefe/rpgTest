package net.gpdev.autotile

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import net.gpdev.autotile.AutoTiler
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import net.gpdev.autotile.GameScreen
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.elfefe.rpgtest.utils.GameConfig
import kotlin.math.max

class GameScreen : ScreenAdapter() {
    private lateinit var map: TiledMap
    private lateinit var camera: OrthographicCamera
    private lateinit var guiCam: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var screenViewport: ScreenViewport
    private lateinit var renderer: OrthogonalTiledMapRenderer
    private lateinit var autoTiler: AutoTiler
    private lateinit var font: BitmapFont
    private val layout = GlyphLayout()
    private lateinit var batch: SpriteBatch
    private var elapsedTime = 0f
    override fun show() {
        super.show()

        // Setup camera
        camera = OrthographicCamera()
        viewport = FitViewport(CAMERA_WIDTH.toFloat(), CAMERA_HEIGHT.toFloat(), camera)

        // Setup GUI camera
        guiCam = OrthographicCamera()
        screenViewport = ScreenViewport(guiCam)
        guiCam.setToOrtho(false)

        // Setup font rendering
        batch = SpriteBatch()
        font = BitmapFont(Gdx.files.internal("arial-15.fnt"), false)
        font.color = PROMPT_COLOR
        layout.setText(font, PROMPT_TEXT)

        // Auto generate a new map
        autoTiler = AutoTiler(MAP_WIDTH, MAP_HEIGHT, Gdx.files.internal("tileset.json"))
//        map = autoTiler.generateMap()

        // Setup map renderer
        val unitScale = 1f / max(autoTiler.tileWidth, autoTiler.tileHeight)
        renderer = OrthogonalTiledMapRenderer(map, unitScale)

        // Setup input processor
//        Gdx.input.setInputProcessor(new InputAdapter() {
//            @Override
//            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//                // Generate a new procedural map on touch event
//                map = autoTiler.generateMap();
//                elapsedTime = 0;
//                return true;
//            }
//        });
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height)
        screenViewport.update(width, height)
    }

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Render map
        viewport.apply(true)
        renderer.setView(camera)
        renderer.render()
        elapsedTime += delta

        // Render text prompt
        screenViewport.apply(true)
        batch.projectionMatrix = guiCam.combined
        batch.begin()
        font.setColor(PROMPT_COLOR.r, PROMPT_COLOR.g, PROMPT_COLOR.b,
                (elapsedTime - PROMPT_FADE_IN) % PROMPT_FADE_OUT)
        font.draw(batch, PROMPT_TEXT,
                (screenViewport.screenWidth - layout.width) / 2.0f,
                screenViewport.screenHeight - layout.height)
        batch.end()
    }

    override fun dispose() {
        super.dispose()
        map.dispose()
        font.dispose()
        batch.dispose()
    }

    companion object {
        private val MAP_WIDTH = GameConfig.config.get(GameConfig.MAP).get(GameConfig.WIDTH).asInt()
        private val MAP_HEIGHT = GameConfig.config.get(GameConfig.MAP).get(GameConfig.HEIGHT).asInt()
        private val CAMERA_WIDTH = GameConfig.config.get(GameConfig.CAMERA).get(GameConfig.WIDTH).asInt()
        private val CAMERA_HEIGHT = GameConfig.config.get(GameConfig.CAMERA).get(GameConfig.HEIGHT).asInt()
        private const val PROMPT_TEXT = "Click anywhere to generate a new map"
        private val PROMPT_COLOR = Color.CORAL
        private const val PROMPT_FADE_IN = 2f
        private const val PROMPT_FADE_OUT = 4f
    }
}