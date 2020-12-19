package com.elfefe.rpgtest.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.model.Entity

var ids: Int = 0
    get() = ++field

val entities = ArrayList<Entity>()
var stateTime = 0f

val clickPosition = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())