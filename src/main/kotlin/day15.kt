@file:Suppress("DuplicatedCode")

import kotlin.system.measureTimeMillis

fun main() {
    val day = 15
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line ->
            line.map { elem ->
                elem.digitToInt()
            }
        }
    }

    fun fourAround(coords: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (x, y) = coords
        return listOf(
            (x-1 pairWith y  ),
            (x   pairWith y-1),
            (x+1 pairWith y  ),
            (x   pairWith y+1)
        )
    }

    // board to score
    fun lowestPath(board: List<List<Int>>): Int {
        // pairs store X,Y
        var paths = mutableListOf(Pair(0, Pair(0,0)))

        val visited = mutableSetOf(Pair(0, 0))
        do {
            val currRoute = paths.removeFirst()
//            println(currRoute)
            if (currRoute.second == Pair(board[0].size-1, board.size-1))
                return currRoute.first

            fourAround(currRoute.second)
                .filter { it !in visited}
                .filter { it xycoordInListOfLists board }
                .forEach { newLocation ->
                    paths.add(Pair(
                        currRoute.first + board[newLocation.second][newLocation.first],
                        newLocation
                        ))
                    visited.add(newLocation)
                }
            paths = paths.sortedBy { it.first }.toMutableList()
        } while (true)
    }

    fun part1(inputLines: List<String>): String {
        return lowestPath(parseInput(inputLines)).toString()
    }

    fun multRow(row: List<Int>): List<Int> {
        val result = row.toMutableList()
        repeat(4) { iteration ->
            row.forEach { origElem ->
                var newVal = origElem + iteration + 1
                if (newVal > 9)
                    newVal -= 9
                result.add(newVal)
            }
        }
        return result
    }

    fun multBoard(board: List<List<Int>>): List<List<Int>> {
        val expandedRows = board.map { multRow(it) }
        return expandedRows.transpose().map{multRow(it)}.transpose()
    }

    fun part2(inputLines: List<String>): String {
        return lowestPath(multBoard(parseInput(inputLines))).toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "40")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"755") // too low: 754

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "315")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"3016")

    
    println("Time one : " + measureTimeMillis { part1(data) })
    println("time expa: " + measureTimeMillis { multBoard(parseInput(data)) })
    println("Time two : " + measureTimeMillis { part2(data) })

    println("Time once: " + measureTimeMillis { part1(data); part2(data) })
    println("Time once: " + measureTimeMillis { part1(data); part2(data) })

    println("The time for part 1 and 2 (10X) without reading but with parsing (twice for each run)")
    println("Time (ms): " + measureTimeMillis {
        repeat(10) {
            part1(data)
            part2(data)
            }
        }
    )
}

// assuming X Y coords
private infix fun <B> Pair<Int, Int>.xycoordInListOfLists(listOLists: List<List<B>>): Boolean {
    return this.second < listOLists.size &&
            this.first < listOLists[0].size &&
            this.first >= 0 &&
            this.second >= 0
}
