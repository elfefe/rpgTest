package com.elfefe.rpgtest.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.elfefe.rpgtest.utils.noise.MapNoise


/***
 * Generate an array noise between 0 and 1
 *
 * @param width The array width
 * @param height The array height
 *
 * *************** @author Donelfefe *******************/

class MapGenerator {
    val shapeRenderer = ShapeRenderer()

    var mapProcedural = Pixmap(Gdx.graphics.width, Gdx.graphics.height, Pixmap.Format.RGBA8888)
    var generated = MapNoise.generatePerlinNoise(mapProcedural.width, mapProcedural.height, 8, 24)

    fun draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Point)
        for (i in 0 until mapProcedural.width * mapProcedural.height) {
            val x = fastFloor(i / mapProcedural.height / 1.0)
            val y = i - (x * mapProcedural.height)
            val c = generated[x][y]
            shapeRenderer.run {
                point(x.toFloat(), y.toFloat(), 0f)
                color = c.color
            }
        }
        shapeRenderer.end()
    }

    fun dispose() = shapeRenderer.dispose()
}