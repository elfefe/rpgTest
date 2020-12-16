package com.elfefe.rpgtest.utils.raycasting

import com.badlogic.gdx.math.Vector2
import java.lang.Float.MAX_VALUE
import java.util.*
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

object RayCast {
    private const val EPSILON = 0.000001

    // Find intersection of RAY & SEGMENT
    // returns null if no intersection found
    private fun getIntersection(ray: LineSegment, segment: LineSegment): Vector2? {
        val r = Vector2(ray.B.x - ray.A.x, ray.B.y - ray.A.y)
        val s = Vector2(segment.B.x - segment.A.x, segment.B.y - segment.A.y)
        val rxs = r.crs(s)
        val qp = Vector2(segment.A.x - ray.A.x, segment.A.y - ray.A.y)
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
            Vector2(floor(ray.A.x + t * r.x).toFloat(), floor(ray.A.y + t * r.y).toFloat())

            // An intersection was found.
            //return true;
        } else null

        // 5. Otherwise, the two line segments are not parallel but do not intersect.
    }

    fun getClosestIntersection(ray: LineSegment, segments: ArrayList<LineSegment>): Vector2? {
        var closestIntersect: Vector2? = null
        var closestDistance: Float = MAX_VALUE
        for (l in segments) {
            val intersect = getIntersection(ray, l) ?: continue
            if (closestIntersect == null || ray.A.dst(intersect)< closestDistance) {
                closestIntersect = intersect
                closestDistance = ray.A.dst(intersect)
            }
        }
        for (l in segments) {
            val intersect = getIntersection(l, ray) ?: continue
            if (closestIntersect == null || ray.A.dst(intersect) < closestDistance) {
                closestIntersect = intersect
                closestDistance = ray.A.dst(intersect)
            }
        }
        return closestIntersect
    }
}