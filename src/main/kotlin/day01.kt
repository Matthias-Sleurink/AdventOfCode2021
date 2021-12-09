@file:Suppress("DuplicatedCode")

fun main() {

    fun parseInput(input: List<String>): List<Int> {
        return input.map {it.toInt()}
    }

    fun part1(inputLines: List<String>): String {
        val input = parseInput(inputLines)
        return input
            .zipWithNext()
            .count { (curr, next) -> curr < next }
            .toString()
    }


    fun part2(inputLines: List<String>): String {
        val input = parseInput(inputLines)
        return input
            .asSequence()
            .windowed(3, 1, false)
            .map{it.sum()}
            .zipWithNext()
            .map { (first, second) -> first < second }
            .count{it}
            .toString()
    }

    val testData = readTestFileForDay(1)
    testResult(part1(testData), "7")

    val data = readFileForDay(1)
    println("Result 1: " + part1(data))

    testResult(part2(testData), "5")
    println("Result 2: " + part2(data))
}