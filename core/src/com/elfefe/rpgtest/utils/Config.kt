package com.elfefe.rpgtest.utils

import com.elfefe.rpgtest.model.Entity

var ids: Int = 0
    get() = ++field

val entities = ArrayList<Entity>()