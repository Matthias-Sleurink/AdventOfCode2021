@file:Suppress("DuplicatedCode")

fun main() {
    fun parseInput(input: List<String>): Sequence<Pair<String, Int>> {
        return input
            .asSequence()
            .map {
                Pair(it.substringBefore(' '),
                     it.substringAfter(' ').toInt())
            }
    }

    fun part1(inputLines: List<String>): String {
        return parseInput(inputLines).fold(Pair(0,0)) {
            (horizontal, depth), (command, distance) ->
            when (command) {
                "forward" -> Pair(horizontal + distance, depth)
                "down" -> Pair(horizontal, depth + distance)
                "up" -> Pair(horizontal, depth - distance)
                else -> {throw IllegalStateException("Found command $command")}
            }
        }.run { first * second }.toString()
    }


    fun part2(inputLines: List<String>): String {
        return parseInput(inputLines).fold(Triple(0,0, 0)) {
            (horizontal, depth, aim), (command, amount) ->
            when (command) {
                "forward" -> Triple(horizontal + amount, depth + (aim * amount), aim)
                "down" -> Triple(horizontal, depth, aim + amount)
                "up" -> Triple(horizontal, depth, aim - amount)
                else -> {throw IllegalStateException("Found command $command")}
            }
        }.run { first * second }.toString()
    }

    val testData = readTestFileForDay(2)
    testResult(part1(testData), "150")

    val data = readFileForDay(2)
    println("Result 1: " + part1(data))

    testResult(part2(testData), "900")
    println("Result 2: " + part2(data))
}