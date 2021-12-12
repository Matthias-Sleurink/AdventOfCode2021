@file:Suppress("DuplicatedCode")

import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    val day = 12
    fun parseInput(input: List<String>): Map<String, List<String>> {
        return input.asSequence().map {
            it.split("-")
        }.map { (from, to) ->
            if ((from=="start") or (to == "end"))  listOf(from pairWith to) else listOf(from pairWith to, to pairWith from)
        }.flatten().groupBy {it.first
        }.map { (from, routes) ->
            from pairWith routes.map { it.second }.distinct()
        }.toMap()
    }

    fun routesFrom(tillNow: Stack<String>, end: String, map: Map<String, List<String>>, visitOnce: Set<String>): List<List<String>> {
        if (tillNow.peek() == end)
            return listOf(tillNow.toList())

        return map[tillNow.peek()]!!.mapNotNull {
            if (it.isLowercase()) {
                // if it is only allowed once and it is in there once or if it is in there twice
                if ((it in tillNow && it in visitOnce) || tillNow.count { nodeIn -> it == nodeIn } > 1)
                    return@mapNotNull null
                tillNow.push(it)
                routesFrom(tillNow, end, map, visitOnce).also { tillNow.pop() }
            } else { // uppercase
                // we are allowed to return to this node since it is uppercase
                tillNow.push(it)
                routesFrom(tillNow, end, map, visitOnce).also { tillNow.pop() }
            }
        }.flatten()
    }

    fun part1(inputLines: List<String>): String {
        val map = parseInput(inputLines)
        val tillNow = stackOf("start")
        return routesFrom(tillNow, "end", map, map.keys.filter { it.isLowercase() }.toSet()).count().toString()
    }

    fun part2(inputLines: List<String>): String {
        val map = parseInput(inputLines)
        val tillNow = stackOf("start")

        val nodes = map.keys.filter { it.isLowercase() }

        // this could be made a lot faster (I think) by spawning two versions of a route anywhere we see a node that we _may_ visit twice.
        //  I believe that we could make that in such a way that we only have to carry a bool with us (didDoubleNode) instead of this whole Set and the double check.
        val visitOnceVersions = nodes.filter{it != "start" && it != "end"}.map { node ->
            map.keys.filterNot { it == node || !it.isLowercase() }.toSet()
        }

        return visitOnceVersions.
                        map {routesFrom(tillNow, "end", map, it)}
                            .flatten()
                            .distinct()
                            .count()
                            .toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "10")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"3708")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "36")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"93858")

    println("The time for part 1 and 2 (10X) without reading but with parsing (twice for each run)")
    println("Time (ms): " + measureTimeMillis {
        repeat(10) {
            part1(data)
            part2(data)
            }
        }
    )
}
