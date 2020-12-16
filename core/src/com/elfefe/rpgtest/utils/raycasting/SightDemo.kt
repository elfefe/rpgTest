package com.elfefe.rpgtest.utils.raycasting

import com.badlogic.gdx.math.Vector2
import com.elfefe.rpgtest.utils.raycasting.RayCast.getClosestIntersection
import java.awt.Color
import java.awt.Graphics
import java.awt.Polygon
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by Armin on 9/21/2017.
 */
class SightDemo : JPanel(), MouseMotionListener {
    var activePolygons = ArrayList<Polygon>()
    private fun initPolygons() {

        //Border Polygon
        val b = Polygon()
        b.addPoint(0, 0)
        b.addPoint(640, 0)
        b.addPoint(640, 360)
        b.addPoint(0, 360)
        activePolygons.add(b)
        val p1 = Polygon()
        p1.addPoint(100, 150)
        p1.addPoint(120, 50)
        p1.addPoint(200, 80)
        p1.addPoint(140, 210)
        activePolygons.add(p1)
        val p2 = Polygon()
        p2.addPoint(100, 200)
        p2.addPoint(120, 250)
        p2.addPoint(60, 300)
        activePolygons.add(p2)
        val p3 = Polygon()
        p3.addPoint(200, 260)
        p3.addPoint(220, 150)
        p3.addPoint(300, 200)
        p3.addPoint(350, 320)
        activePolygons.add(p3)
        val p4 = Polygon()
        p4.addPoint(340, 60)
        p4.addPoint(360, 40)
        p4.addPoint(370, 70)
        activePolygons.add(p4)
        val p5 = Polygon()
        p5.addPoint(450, 190)
        p5.addPoint(560, 170)
        p5.addPoint(540, 270)
        p5.addPoint(430, 290)
        activePolygons.add(p5)
        val p6 = Polygon()
        p6.addPoint(400, 95)
        p6.addPoint(580, 50)
        p6.addPoint(480, 150)
        p6.addPoint(400, 95)
        activePolygons.add(p6)
    }

    var activeSegments = ArrayList<LineSegment>()
    private fun initSegments() {
        for (p in activePolygons) {
            for (i in 0 until p.npoints) {
                val start = Vector2(p.xpoints[i].toFloat(), p.ypoints[i].toFloat())
                var end: Vector2
                end = if (i == p.npoints - 1) {
                    Vector2(p.xpoints[0].toFloat(), p.ypoints[0].toFloat())
                } else {
                    Vector2(p.xpoints[i + 1].toFloat(), p.ypoints[i + 1].toFloat())
                }
                activeSegments.add(LineSegment(start, end))
                //System.out.println("new segment : " + start + " -> " + end);
            }
        }
    }

    var activePoints = ArrayList<Vector2>()
    private fun initPoints() {
        for (poly in activePolygons) {
            for (i in 0 until poly.npoints) {
                activePoints.add(Vector2(poly.xpoints[i].toFloat(), poly.ypoints[i].toFloat()))
            }
        }
    }

    var mousePos = Vector2(1f, 1f)
    override fun mouseMoved(e: MouseEvent) {
        mousePos = Vector2(e.x.toFloat(), e.y.toFloat())
        repaint()
    }

    fun castRays(src: Vector2, n: Int, dist: Int): ArrayList<Vector2> {
        val result = ArrayList<Vector2>()
        val angle_div = 2 * Math.PI / n
        for (i in 0 until n) {
            val target = Vector2((src.x + cos(angle_div * i) * dist).toInt().toFloat(), (src.y + sin(angle_div * i) * dist).toInt().toFloat())
            val ray = LineSegment(src, target)
            val ci: Vector2? = getClosestIntersection(ray, activeSegments)
            if (ci != null) result.add(ci) else result.add(target)
        }
        return result
    }

    fun castRaysToCornersOnly(src: Vector2, dist: Int): ArrayList<Vector2> {
        val angles = ArrayList<Double>()
        for (pt in activePoints) {
            val angle = Math.atan2((pt.y - src.y).toDouble(), (pt.x - src.x).toDouble())
            //uniquePoint.angle = angle;
            angles.add(angle - 0.01)
            angles.add(angle)
            angles.add(angle + 0.01)
        }
        angles.sort()
        val result = ArrayList<Vector2>()
        //double angle_div = 2 * Math.PI / n;
        for (a in angles) {
            val target = Vector2((src.x + cos(a) * dist).toInt().toFloat(), (src.y + sin(a) * dist).toInt().toFloat())
            val ray = LineSegment(src, target)
            val ci: Vector2? = getClosestIntersection(ray, activeSegments)
            if (ci != null) result.add(ci) else result.add(target)
        }
        return result
    }

    fun getSightPolygon(src: Vector2): Polygon {
        val currentRays = castRaysToCornersOnly(src, 800)
        val giantPoly = Polygon()
        for (p in currentRays) {
            giantPoly.addPoint(p.x.toInt(), p.y.toInt())
        }
        return giantPoly
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        g.color = Color.WHITE
        for (p in activePolygons) {
            g.drawPolygon(p)
        }
        val mainSightPoly = getSightPolygon(mousePos)

        //Circular Fuzzy
        val fuzzySightPoly = ArrayList<Polygon>()
        val fradius = 5
        val fcount = 10
        val fanglediv = Math.PI * 2 / fcount
        for (i in 0 until fcount) {
            val dx = sin(fanglediv * i) * fradius
            val dy = cos(fanglediv * i) * fradius
            fuzzySightPoly.add(getSightPolygon(Vector2(mousePos.x + dx.toInt(), mousePos.y + dy.toInt())))
        }

        //Linear Fuzzy
        /*int flen = 50;
        double dx = flen / 10;
        for (int i = -10 ; i < 10 ; i++) {
            double x = dx * i;
            fuzzySightPoly.add(getSightPolygon(new Vector2(mousePos.x,mousePos.y+(int)x)));
        }*/
        g.color = Color(255, 255, 255, 65)
        for (fsp in fuzzySightPoly) {
            g.fillPolygon(fsp)
        }
        //g.setColor(Color.WHITE);
        //g.fillPolygon(mainSightPoly);
    }

    override fun mouseDragged(e: MouseEvent) {}

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val window = JFrame()
            window.title = "Line of Sight Demo"
            window.setSize(640, 380)
            val sd = SightDemo()
            window.add(sd)
            window.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            window.isVisible = true
        }
    }

    init {
        background = Color.BLACK
        this.layout = null
        initPolygons()
        initSegments()
        initPoints()
        addMouseMotionListener(this)
    }
}