@file:Suppress("DuplicatedCode")

import java.util.*

enum class FoldAxis {
    X, Y
}

fun main() {
    val day = 13

    fun parseInput(input: List<String>): Pair<List<Pair<FoldAxis, Int>>, MutableMap<Pair<Int, Int>, Boolean>> {
        return input.filterNot { it.isEmpty() }.partition {
            it.startsWith("fold along")
        }.let { (folds, coordinates) ->
            folds.map { it.split("=").let {(foldPart, number) -> (if (foldPart.last() == 'x') FoldAxis.X else FoldAxis.Y) pairWith number.toInt() }
            } pairWith coordinates.associate {
                it.toIntPair() pairWith true
            }.toMutableMap()
        }
    }

    fun MutableMap<Pair<Int, Int>, Boolean>.fold(axis: FoldAxis, foldLine: Int) {
        val add = LinkedList<Pair<Int, Int>>()
        val remove = LinkedList<Pair<Int, Int>>()
        this.forEach { (x, y), isFilled ->
            if (!isFilled)
                return@forEach
            when (axis) {
                FoldAxis.X -> {
                    if (x > foldLine) {
//                        println("Folding over X $foldLine: ($x, $y) to (${foldLine - (x - foldLine)}, $y)")
                        add.add(foldLine - (x - foldLine) pairWith y)
                        remove.add(x pairWith y)
                    }
                }
                FoldAxis.Y -> {
                    if (y > foldLine) {
//                        println("Folding over Y $foldLine: ($x, $y) to ($x, ${foldLine - (y- foldLine)})")
                        add.add(x pairWith foldLine - (y - foldLine))
                        remove.add(x pairWith y)
                    }
                }
            }
        }
        remove.forEach {
            this.remove(it)
        }
        add.forEach {
            this[it] = true
        }
    }


    fun MutableMap<Pair<Int, Int>, Boolean>.draw(): String {
        val xmax = this.keys.maxOf { it.first } + 1
        val ymax = this.keys.maxOf { it.second } + 1
        val board = Array(ymax) { Array(xmax) { false } }

        this.filter { it.value }.keys.forEach { (x, y)->
            board[y][x] = true
        }

        val result = StringBuilder(xmax * ymax * 2)
        board.forEach { row ->
            row.forEach { colElem ->
                result.append(if (colElem) "▉▉▉" else "   ")
            }
            result.append("\n")
        }
        return result.toString()
    }


    fun part1(inputLines: List<String>): String {
        val (folds, map) = parseInput(inputLines)
        folds.first().let { (axis, coordinate) ->
            map.fold(axis, coordinate)
        }
        return map.values.count { it }.toString()
    }

    fun part2(inputLines: List<String>): String {
        val (folds, map) = parseInput(inputLines)
        folds.forEach { (axis, coordinate) ->
            map.fold(axis, coordinate)
        }
        println(map.draw())
//        print("Input pls: ")
        return "Part 2"
//        return readLine()!!
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "17")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"759")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "Part 2")
//    testResult(part2test, "O")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res, "Part 2")
//    testFinalResult(part2res,"HECRZKPR")

//    println("The time for part 1 and 2 (1000X) without reading but with parsing (twice for each run)")
//    println("Time (ms): " + measureTimeMillis {
//        repeat(1000) {
//            part1(data)
//            part2(data)
//            }
//        }
//    )
}


