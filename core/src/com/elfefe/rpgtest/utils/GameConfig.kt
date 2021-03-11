package com.elfefe.rpgtest.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

object GameConfig {
    var FILE_NAME = "GameConfig.json"
    val MAP = "Map"
    val CAMERA = "Camera"

    val WIDTH = "Width"
    val HEIGHT = "Height"
    val config: JsonValue = JsonReader().parse(Gdx.files.local(FILE_NAME))
}