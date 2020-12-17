package com.elfefe.rpgtest.utils.raycasting

import com.badlogic.gdx.math.Vector2
import java.lang.Float.MAX_VALUE
import java.util.*
import kotlin.math.*

object RayCast {
    private const val EPSILON = 0.000001

    // Find intersection of RAY & SEGMENT
    // returns null if no intersection found
    private fun getIntersection(ray: LineSegment, segment: LineSegment): Vector2? {

        val r = ray.B.cpy().sub(ray.A)
        val s = segment.B.cpy().sub(segment.A)
        val rxs = r.crs(s)
        val qp = segment.A.cpy().sub(ray.A)
        val qpxr = qp.crs(r)

        // If r x s = 0 and (q - p) x r = 0, then the two lines are collinear.
        if (rxs < EPSILON && qpxr < EPSILON) {
            // 1. If either  0 <= (q - p) * r <= r * r or 0 <= (p - q) * s <= * s
            // then the two lines are overlapping,
            /*if (considerCollinearOverlapAsIntersect)
                if ((0 <= (q - p)*r && (q - p)*r <= r*r) || (0 <= (p - q)*s && (p - q)*s <= s*s))
                    return true;*/

            // 2. If neither 0 <= (q - p) * r = r * r nor 0 <= (p - q) * s <= s * s
            // then the two lines are collinear but disjoint.
            // No need to implement this expression, as it follows from the expression above.
            return null
        }

        // 3. If r x s = 0 and (q - p) x r != 0, then the two lines are parallel and non-intersecting.
        if (rxs < EPSILON && qpxr >= EPSILON) return null

        // t = (q - p) x s / (r x s)
        //var t = (q - p).Cross(s)/rxs;
        val t = qp.crs(s) / rxs
        // u = (q - p) x r / (r x s)
        //var u = (q - p).Cross(r)/rxs;
        val u = qp.crs(r) / rxs
        // 4. If r x s != 0 and 0 <= t <= 1 and 0 <= u <= 1
        // the two line segments meet at the Vector2 p + t r = q + u s.
        return if (rxs >= EPSILON && 0 <= t && t <= 1 && 0 <= u && u <= 1) {
            // We can calculate the intersection Vector2 using either t or u.
            //intersection = p + t*r;
            Vector2(floor(ray.A.x + t * r.x), floor(ray.A.y + t * r.y))

            // An intersection was found.
            //return true;
        } else null
        // 5. Otherwise, the two line segments are not parallel but do not intersect.
    }

    fun getClosestIntersection(ray: LineSegment, segments: ArrayList<LineSegment>): Vector2? {
        var closestIntersect: Vector2? = null
        var closestDistance: Float = MAX_VALUE
        for (l in segments) {
            val intersect = getIntersection(ray, l) ?: getIntersection(l, ray) ?: continue
            if (closestIntersect == null || ray.A.dst(intersect) < closestDistance) {
                closestIntersect = intersect
                closestDistance = ray.A.dst(intersect)
            }
        }
        return closestIntersect
    }



    fun castRays(src: Vector2, n: Int, dist: Int, segments: ArrayList<LineSegment>): ArrayList<Vector2> {
        val result = ArrayList<Vector2>()
        val angleDiv = 2 * Math.PI / n
        for (i in 0 until n) {
            val target = Vector2((src.x + cos(angleDiv * i) * dist).toFloat(), (src.y + sin(angleDiv * i) * dist).toFloat())
            val ray = LineSegment(src, target)
            val ci = RayCast.getClosestIntersection(ray, segments)
            if (ci != null) result.add(ci) else result.add(target)
        }
        return result
    }
}