@file:Suppress("DuplicatedCode")

import kotlin.system.measureTimeMillis

fun main() {
    val day = 0
    fun parseInput(input: List<String>): Any {
        return input.asSequence()
    }

    fun part1(inputLines: List<String>): String {
        parseInput(inputLines)
        return "."
    }

    fun part2(inputLines: List<String>): String {
        parseInput(inputLines)
        return "Part 2"
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, ".")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,null)

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "Part 2")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,null)

    println("The time for part 1 and 2 without reading but with parsing (twice)")
    println("Time (ms):" + measureTimeMillis {
        part1(data)
        part2(data)
    })



}