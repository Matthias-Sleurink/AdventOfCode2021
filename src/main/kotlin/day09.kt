@file:Suppress("DuplicatedCode")

import kotlin.system.measureTimeMillis

fun main() {
    val day = 9
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line ->
            line.map { elem ->
                elem.digitToInt()
            }
        }
    }

    fun leftOfIndex(rowIndex: Int, colIndex: Int, grid: List<List<Int>>): Pair<Int, Int>? {
        if (rowIndex == 0) return null
        return Pair(rowIndex-1, colIndex)
    }
    fun leftOf(rowIndex: Int, colIndex: Int, grid: List<List<Int>>): Int? {
        if (rowIndex == 0) return null
        return grid[rowIndex-1][colIndex]
    }

    fun rightOfIndex(ri: Int, ci: Int, grid: List<List<Int>>): Pair<Int, Int>? {
        if (ri == grid.size-1) return null
        return Pair(ri+1, ci)
    }

    fun rightOf(ri: Int, ci: Int, grid: List<List<Int>>): Int? {
        if (ri == grid.size-1) return null
        return grid[ri + 1][ci]
    }
    fun belowOfIndex(ri: Int, ci: Int, grid: List<List<Int>>): Pair<Int, Int>? {
        if (ci == grid[ri].size-1) return null
        return Pair(ri, ci+1)
    }

    fun belowOf(ri: Int, ci: Int, grid: List<List<Int>>): Int? {
        if (ci == grid[ri].size-1) return null
        return grid[ri][ci+1]
    }

    fun aboveOfIndex(ri: Int, ci: Int, grid: List<List<Int>>): Pair<Int, Int>? {
        if (ci == 0) return null
        return Pair(ri, ci-1)
    }

    fun aboveOf(ri: Int, ci: Int, grid: List<List<Int>>): Int? {
        if (ci == 0) return null
        return grid[ri][ci-1]
    }

    fun isLowPoint(ri: Int, ci: Int, grid: List<List<Int>>): Boolean {
        val thisValue = grid[ri][ci]
        val leftOf = leftOf(ri, ci, grid)
        val rightOf = rightOf(ri, ci, grid)
        val belowOf = belowOf(ri, ci, grid)
        val aboveOf = aboveOf(ri, ci, grid)
        return (leftOf == null || thisValue < leftOf) &&
                (rightOf == null || thisValue < rightOf) &&
                (belowOf == null || thisValue < belowOf) &&
                (aboveOf == null || thisValue < aboveOf)
    }

    fun part1(inputLines: List<String>): String {
        val grid = parseInput(inputLines)
        return grid.mapIndexed rowMap@{rowIndex, row->
            row.mapIndexedNotNull colMap@{ colIndex, i ->
                if (isLowPoint(rowIndex, colIndex, grid)) {
                    return@colMap 1 + i
                } else {
                    return@colMap null
                }
            }.sum()
        }.sum().toString()
    }

    fun getBasin(index: Pair<Int, Int>, grid: List<List<Int>>): Int {
        val basinElements = mutableSetOf(index)
        val toSearch = mutableSetOf(index)
        while (toSearch.isNotEmpty()) {
            val (ri, ci) = toSearch.removeFirst()
            val curr = grid[ri][ci]
            if (curr == 9) continue

            basinElements.add(Pair(ri, ci))

            val leftOf = leftOfIndex(ri, ci, grid)
            if (leftOf != null && !basinElements.contains(leftOf)) toSearch.add(leftOf)

            val rightOf = rightOfIndex(ri, ci, grid)
            if (rightOf != null && !basinElements.contains(rightOf)) toSearch.add(rightOf)

            val belowOf = belowOfIndex(ri, ci, grid)
            if (belowOf != null && !basinElements.contains(belowOf)) toSearch.add(belowOf)

            val aboveOf = aboveOfIndex(ri, ci, grid)
            if (aboveOf != null && !basinElements.contains(aboveOf)) toSearch.add(aboveOf)
        }
        return basinElements.size
    }

    fun part2(inputLines: List<String>): String {
        val grid = parseInput(inputLines)
        return grid.asSequence().mapIndexed rowMap@{ rowIndex, row ->
            row.mapIndexedNotNull colMap@{ colIndex, _ ->
                if (isLowPoint(rowIndex, colIndex, grid)) {
                    return@colMap Pair(rowIndex, colIndex)
                } else {
                    return@colMap null
                }
            }
        }.flatten().map { lowest ->
            getBasin(lowest, grid)
        }.sortedDescending()
        .take(3)
        .map(Int::toLong)
        .reduce(Long::times).toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "15")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"541")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "1134")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"847504")

    println("The time for part 1 and 2 without reading but with parsing (twice)")
    println("Time (ms):" + measureTimeMillis {
        part1(data)
        part2(data)
    })

}

