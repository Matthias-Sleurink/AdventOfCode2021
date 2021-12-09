@file:Suppress("DuplicatedCode")

import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    val day = 5

    class Line(val start: Pair<Int, Int>, val end: Pair<Int, Int>, val ignoreDiag: Boolean = false) {
        fun allPoints(): Sequence<Pair<Int, Int>> {
            val list = mutableListOf<Pair<Int, Int>>()
            if (start.first == end.first) {
                list.addAll((min(start.second, end.second)..max(start.second, end.second)).map { Pair(start.first, it) })
            } else if (start.second == end.second) {
                list.addAll((min(start.first, end.first)..max(start.first, end.first)).map { Pair(it, start.second) })
            } else {
                if (!ignoreDiag) {
                    list.addAll((start.first toward end.first).zip(start.second toward end.second))
                }
            }
            return list.asSequence()
        }

        override fun hashCode(): Int {
            return Pair(start, end).hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return Pair(start, end) == other
        }
    }

//    println(Line(Pair(1,1), Pair(1,3)).allPoints().toList())
//    println(Line(Pair(9,7), Pair(7,7)).allPoints().toList())

//    println(Line(Pair(1,1), Pair(3,3)).allPoints().toList())
//    println(Line(Pair(9,7), Pair(7,9)).allPoints().toList())

    class Board constructor (lines: List<Line>, val board: MutableMap<Pair<Int, Int>, Int> = Counter()) {
        init {
            lines.forEach { line ->
                line.allPoints().forEach {
                    board[it] = board.getValue(it) + 1
            } }
        }
    }

    fun parseInput(input: List<String>, ignoreDiag: Boolean = false): Board {
        return Board(input.map { line ->
            line.split(" -> ")
                .map{ coordinateStr ->
                    coordinateStr.split(",")
                }.map {
                    Pair(it[0].toInt(), it[1].toInt())
                }
        }.map {
            Line(it[0], it[1], ignoreDiag)
        })
    }

    fun part1(inputLines: List<String>): String {
        return parseInput(inputLines, ignoreDiag = true).board.values.count { it >= 2 }.toString()
    }

    fun part2(inputLines: List<String>): String {
        return parseInput(inputLines).board.values.count { it >= 2 }.toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "5")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"6572")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "12")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"21466")
}