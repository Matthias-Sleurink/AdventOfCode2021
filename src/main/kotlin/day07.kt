@file:Suppress("DuplicatedCode")

import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

fun main() {
    val day = 7
    fun parseInput(input: List<String>): List<Int> {
        return input[0].split(",").map{it.toInt()}
    }

    fun part1(inputLines: List<String>): String {
        val locations = parseInput(inputLines)
        val max = locations.maxOrNull() ?: TODO()
        val min = locations.minOrNull() ?: TODO()

        // pos, cost
        val minLocation = (min toward max).map { pos ->
            Pair(pos, locations.sumOf { abs(it - pos) })
        }.minByOrNull {it.second} ?: TODO()

        return minLocation.second.toString()
    }

    fun part2(inputLines: List<String>): String {
        val locations = parseInput(inputLines)
        val max = locations.maxOrNull() ?: TODO()
        val min = locations.minOrNull() ?: TODO()

        // pos, cost
        val minLocation = (min toward max).map { pos ->
//            Pair(pos, locations.map { abs(it - pos) }.sumOf { (0 toward it).sum() })
            Pair(pos, locations.map { abs(it - pos) }.sumOf { (it * (it / 2.0) + (it / 2.0)).roundToInt() })
        }.minByOrNull {it.second} ?: TODO()

        return minLocation.second.toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "37")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"354129")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "168")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"98905973")

    println("The time for part 1 and 2 without reading but with parsing (twice)")
    println("Time (ms):" + measureTimeMillis {
        part1(data)
        part2(data)
    })




}