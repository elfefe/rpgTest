package com.elfefe.rpgtest.model

data class Layer(
        var order: Int,
        var physic: ArrayList<Int>,
        var triggerBounds: HashMap<Int, Rectangle>,
        var collisionBounds: ArrayList<Rectangle>
)