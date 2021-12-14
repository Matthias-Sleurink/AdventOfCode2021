@file:Suppress("DuplicatedCode")

import kotlin.system.measureTimeMillis

fun main() {
    val day = 14

    data class Polymer(
        var pairs: MutableMap<String, Long>,
        val transformations: List<Pair<String, List<String>>>,
        var RHS: Char
    ) {
        fun step(n: Int): Polymer {
            repeat(n) { step() }
            return this
        }

        fun step(): Polymer {
            val notVisited = pairs.keys.toMutableSet()
            val newElements = mutableMapOf<String, Long>().withDefault { 0L }
            transformations.forEach { (lhs, rhss) ->
                notVisited.remove(lhs)

                rhss.forEach { rhs ->
                    newElements[rhs] = newElements.getValue(rhs) + pairs.getValue(lhs)
                }
            }
            notVisited.forEach {
                newElements[it] = newElements.getValue(it) + pairs.getValue(it)
            }
            pairs = newElements
            return this
        }

        // We count all the left characters of the pairs. This counts all the overlaps correctly, except that
        //    the last character is not on the left side of any pair.
        //    So we hold on to what the rightmost char is and add one of those at the end.
        fun counts(): Map<Char, Long> {
            val counter = this.pairs
                .toList()
                .fold(Counter<Char>()) { c, p ->
                    c.also {
                        it[p.first.first()] = it.getValue(p.first.first()) + p.second
                    }
                }
            counter[RHS] = counter.getValue(RHS) + 1
            return counter
        }
    }

    fun parseInput(input: List<String>): Polymer {
        val pairs = input
            .first()
            .zipWithNext()
            .map { it.first.toString() + it.second }
            .groupBy { it }
            .map { (pair, countHolder) -> pair pairWith countHolder.count().toLong() }
            .toMutableMap().withDefault { 0L }
        val reactions = input
            .drop(2)
            .map { it.split(" -> ") }
            .map { it.toPair() }
            .map { (lhs, rhs) ->
                lhs pairWith listOf(lhs.first() + rhs, rhs + lhs.second())
            }

        return Polymer(pairs, reactions, pairs.toList().last().first.second())
    }

    fun part1(inputLines: List<String>): String {
        return parseInput(inputLines)
            .step(10)
            .counts()
            .asSequence()
            .sortedBy { it.value }.let { it.last().value - it.first().value }.toString()
    }

    fun part2(inputLines: List<String>): String {
        return parseInput(inputLines)
            .step(40)
            .counts()
            .asSequence()
            .sortedBy { it.value }.let { it.last().value - it.first().value }.toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "1588")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res, "3411")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "2188189693529")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res, "7477815755570")

    println("The time for part 1 and 2 (1000X) without reading but with parsing (twice for each run)")
    println("Time (ms): " + measureTimeMillis {
        repeat(1000) {
            part1(data)
            part2(data)
        }
    })
}

