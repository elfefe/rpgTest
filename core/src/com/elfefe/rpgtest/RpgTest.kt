package com.elfefe.rpgtest

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.elfefe.rpgtest.model.Entity
import com.elfefe.rpgtest.model.House
import com.elfefe.rpgtest.model.Personnage
import com.elfefe.rpgtest.model.Player
import com.elfefe.rpgtest.utils.*
import com.elfefe.rpgtest.utils.raycasting.LineSegment
import net.gpdev.autotile.AutoTiler
import java.util.ArrayList
import javax.sound.sampled.Line
import kotlin.math.max


class RpgTest : ApplicationAdapter() {
    lateinit var map: MapManager

    override fun create() {
        map = MapManager()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        map.recalculate(width, height)
    }

    override fun render() {
        map.render()
    }

    override fun dispose() {
        super.dispose()
        map.dispose()
    }
}