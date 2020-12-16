package com.elfefe.rpgtest.utils.raycasting

import com.badlogic.gdx.math.Vector2

class LineSegment(var A: Vector2, var B: Vector2) {
    var dir: Vector2 = Vector2(B.x - A.x, B.y - A.y)
}