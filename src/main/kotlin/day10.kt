@file:Suppress("DuplicatedCode")

import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    val day = 10
    fun parseInput(input: List<String>): Sequence<String> {
        return input.asSequence()
    }

    fun pointsFor(str: Char): Int = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    ).getOrElse(str) { throw IllegalStateException("No score for $str") }

    fun toCanonForm(str: Char): Char = mapOf(
        '(' to '(', ')' to '(',
        '[' to '[', ']' to '[',
        '{' to '{', '}' to '{',
        '<' to '<', '>' to '<'
    ).getOrElse(str ) { throw IllegalStateException("No canon form for $str") }

    fun isOpen(str: Char): Boolean = listOf('(', '{', '[', '<').contains(str)
    fun isClose(str: Char): Boolean = listOf(')', '}', ']', '>').contains(str)

    fun part1(inputLines: List<String>): String {
        val curStack = Stack<Char>()
        return parseInput(inputLines).map {
            it.mapNotNull { char ->
                if (isOpen(char)) {
                    curStack.push(char)
                    return@mapNotNull null
                } else if (isClose(char)) {
                    if (curStack.pop() != toCanonForm(char)) {
                        return@mapNotNull pointsFor(char)
                    } else {
                        return@mapNotNull null
                    }
                } else {
                    throw IllegalStateException("Brace found: $char")
                    // return@mapNotNull null
                }
            }.firstOrNull()
        }.filterNotNull().sum().toString()
    }

    fun completeScoreInc(str: Char): Long = mapOf(')' to 1L, ']' to 2, '}' to 3, '>' to 4)[str]!!
    //{ throw IllegalStateException("No complete score for $str") }


    fun toReverseCanon(str: Char): Char = mapOf(
        '(' to ')', ')' to ')',
        '[' to ']', ']' to ']',
        '{' to '}', '}' to '}',
        '<' to '>', '>' to '>'
    )[str]!!
    //{ throw IllegalStateException("No reverse canon form for $str") }


    fun part2(inputLines: List<String>): String {
        return parseInput(inputLines).toList().map {
            val curStack = Stack<Char>()
            if (it.mapNotNull { char ->
                    if (isOpen(char)) {
                        curStack.push(char)
                        return@mapNotNull null
                    } else if (isClose(char)) {
                        val current = curStack.pop()
                        if (current != toCanonForm(char)) {
                            // faulty
                            // println("Found ${toCanonForm(char)} instead of $current")
                            return@mapNotNull "ERROR FOUND"
                        } else {
                            return@mapNotNull null
                        }
                    } else {
                        throw IllegalStateException("Brace found: $char")
                        // return@mapNotNull null
                    }
                }.isEmpty()) {
                // we found no errors
                if (curStack.isNotEmpty()) {
                    // we would have needed to fill more
                    return@map curStack.asReversed().map { missingChar ->
                        completeScoreInc(toReverseCanon(missingChar))
                    }.fold(0L) { acc, score -> (acc * 5) + score }
                } else {
                    // complete or error
                    return@map null
                }
            } else {
                // error
                return@map null
            }
        }.filterNotNull().median().toString()
    }

    val pairs = listOf("[]", "()", "{}", "<>")
    val pointmap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    fun part1_2(inputLines: List<String>): String {
        return inputLines.map { line ->
            generateSequence(line) { prev ->
                pairs.fold(prev) { acc, bracePair ->
                    acc.replace(bracePair, "")
                }
            }.zipWithNext().takeWhile { it.first != it.second }.last().second
        }.filter { it.isNotEmpty()
        }.mapNotNull { it.toCharArray().firstOrNull { c -> isClose(c) }
        }.sumOf { pointmap[it]!!
        }.toString()
    }

    fun part2_2(inputLines: List<String>): String {
        return inputLines.asSequence()
        .map { line ->
            generateSequence(line) { prev ->
                pairs.fold(prev) { acc, bracePair ->
                    acc.replace(bracePair, "")
                }
            }.zipWithNext().takeWhile { it.first != it.second }.last().second
        }.filter { it.isNotEmpty()
        }.filterNot { it.toCharArray().any { c -> isClose(c)}
        }.map {it.reversed()
        }.map { unMatchedBraces ->
            unMatchedBraces.fold(0L) { acc, char -> (acc * 5) + completeScoreInc(toReverseCanon(char)) }
        }.toList().median().toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "26397")
    val part1_2Test = part1_2(testData)
    println("----- Test result 1_2: $part1_2Test")
    testResult(part1_2Test, "26397")


    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"387363")
    val part1_2res = part1_2(data)
    println("----- Result 1_2: $part1_2res")
    testFinalResult(part1_2res,"387363")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "288957")
    val part2_2test = part2_2(testData)
    println("----- Test result 2_2: $part2_2test")
    testResult(part2_2test, "288957")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"4330777059")
    val part2_2res = part2_2(data)
    println("----- Result 2_2: $part2_2res")
    testFinalResult(part2_2res,"4330777059")


    println("The time for part 1 and 2 without reading but with parsing (twice)")
    println("Time (ms):" + measureTimeMillis {
        part1(data)
        part2(data)
    })

    println("The time for part 1_2 and 2_2 without reading but with parsing (twice)")
    println("Time (ms):" + measureTimeMillis {
        part1_2(data)
        part2_2(data)
    })

}
