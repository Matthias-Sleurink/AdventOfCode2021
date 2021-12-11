@file:Suppress("DuplicatedCode")

import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    val day = 11
    class Octopus(var level: Int, var hasFlashed: Boolean) {
        operator fun component1(): Int {
            return level
        }
        operator fun component2(): Boolean {
            return hasFlashed
        }
        fun canFlash(): Boolean = !hasFlashed && level > 9
    }

    class Board(val board: MutableMap<Pair<Int, Int>, Octopus>) {

        fun stepN(count: Int) = (1 toward count).sumOf { step() }

        fun step(): Int {
            board.onEach { (location, octo) ->
                octo.level += 1
                octo.hasFlashed = false // remove anything leftover from last round
            }

            val toFlash = Stack<Pair<Pair<Int, Int>,Octopus>>()
            board.filter {
                it.value.canFlash()
            }.forEach { (location, octo) ->
                toFlash.push(location pairWith octo)
            }

            while (toFlash.isNotEmpty()) {
                val (location, octopus) = toFlash.pop()
                if (octopus.canFlash()) {
                    octopus.hasFlashed = true
                    octopus.level = 0
                    surroundingFrom(location).forEach {
                        val octoAt = board[it]
                        if (octoAt != null && !octoAt.hasFlashed) {
                            octoAt.level += 1
                            if (octoAt.level > 9)
                                toFlash.push(it to octoAt)
                        }
                    }
                }
            }

            return board.map { (_, octo) ->
                if (octo.hasFlashed) {
                    octo.level = 0
                    return@map true
                }
                return@map false
            }.count { it }

        }

        private fun surroundingFrom(location: Pair<Int, Int>): List<Pair<Int, Int>> {
            val (x, y) = location
            return listOf(
                x-1 to y-1,
                x-1 to y  ,
                x-1 to y+1,
                x   to y-1,
             // x   to y  , // already did this one
                x   to y+1,
                x+1 to y-1,
                x+1 to y  ,
                x+1 to y+1,
            )
        }
    }

    fun parseInput(input: List<String>): Board {
        return Board(input.map { line ->
            line.map { elem ->
                elem.digitToInt()
            }
            }.mapIndexed { ri, row ->
                row.mapIndexed { ci, elem ->
                    (ri pairWith ci) to Octopus(elem, false)
                }
            }.flatten().toMap().toMutableMap())
    }

    fun part1(inputLines: List<String>): String {
        return parseInput(inputLines).stepN(100).toString()

    }

    fun part2(inputLines: List<String>): String {
        return generateSequence(
            parseInput(inputLines) pairWith 0) { it.first pairWith it.first.step() }
            .takeWhile { it.second != 100 }
            .count()
            .toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "1656")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"1694")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "195")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"346")

    println("The time for part 1 and 2 (1000X) without reading but with parsing (twice for each run)")
    println("Time (ms): " + measureTimeMillis {
        repeat(1000) {
            part1(data)
            part2(data)
            }
        }
    )
}
