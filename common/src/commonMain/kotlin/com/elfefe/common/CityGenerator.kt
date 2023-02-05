package com.elfefe.common

import java.util.*

data class Building(var x: Int, var y: Int, var height: Int)
data class Road(var x1: Int, var y1: Int, var x2: Int, var y2: Int)
data class Park(var x: Int, var y: Int, var size: Int)
class CityGenerator {
    var cityBuildings = ArrayList<Building>()
    var cityRoads = ArrayList<Road>()
    var cityParks = ArrayList<Park>()

    var rand = Random()

    var cityWidth = 100
    var cityHeight = 100

    var numBuildings = 100
    var numRoads = 10
    var numParks = 5
    fun generateBuildings() {
        for (i in 0 until numBuildings) {
            val x = rand.nextInt(cityWidth)
            val y = rand.nextInt(cityHeight)
            val height = rand.nextInt(100) + 50
            cityBuildings.add(Building(x, y, height))
        }
    }

    fun generateRoads() {
        for (i in 0 until numRoads) {
            val building1Index = rand.nextInt(cityBuildings.size)
            val building2Index = rand.nextInt(cityBuildings.size)
            val building1 = cityBuildings[building1Index]
            val building2 = cityBuildings[building2Index]
            cityRoads.add(Road(building1.x, building1.y, building2.x, building2.y))
        }
    }

    fun generateParks() {
        for (i in 0 until numParks) {
            val x = rand.nextInt(cityWidth)
            val y = rand.nextInt(cityHeight)
            val size = rand.nextInt(cityWidth / 4) + cityWidth / 8
            // check if park fits in open space and does not overlap with any buildings
            var fitsInOpenSpace = true
            for (building in cityBuildings) {
                if (Math.abs(building.x - x) < size && Math.abs(building.y - y) < size) {
                    fitsInOpenSpace = false
                    break
                }
            }
            if (fitsInOpenSpace) {
                cityParks.add(Park(x, y, size))
            }
        }
    }

    fun generateCity() {
        generateBuildings()
        generateRoads()
        generateParks()
    }
}