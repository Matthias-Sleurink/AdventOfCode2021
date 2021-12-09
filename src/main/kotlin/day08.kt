@file:Suppress("DuplicatedCode")

import kotlin.system.measureTimeMillis

fun main() {
    val day = 8

    class LetterCodes(val numbers: List<String>, val fourOut: List<String>)

    fun parseInput(input: List<String>): List<LetterCodes> {
        return input.map { row ->
            val (left, right) = row.split("|")
                .map { left_right ->
                    left_right.split(" ").filterNot { it.isEmpty() }
                }
            LetterCodes(left, right)
        }
    }

    fun part1(inputLines: List<String>): String {
        return parseInput(inputLines).sumOf {
            it.fourOut.count { outDigit -> listOf(2, 4, 3, 7).contains(outDigit.length) }
        }.toString()
    }

    fun part2(inputLines: List<String>): String {
        return parseInput(inputLines).map { letterCode ->
            LetterCodes(letterCode.numbers.sortedBy { it.length }, letterCode.fourOut)
        }.map{
            letterCodes ->
            LetterCodes(letterCodes.numbers.map { it.sorted() }, letterCodes.fourOut.map { it.sorted() })
        }.map { letterCode ->
            val Sone = letterCode.numbers[0]
            val Sseven = letterCode.numbers[1]
            val Sfour = letterCode.numbers[2]
            val Seight = letterCode.numbers[9]

            val segA = Sseven - Sone
            check(segA.length == 1)

            val segG = letterCode.numbers.mapNotNull { letter ->
                val leftover = letter - Sseven - Sfour
                if (leftover.length == 1)
                    return@mapNotNull leftover
                return@mapNotNull null
            }.first()

            val segE = Seight - Sfour - segA - segG

            val SNine = letterCode.numbers.single {
                it == (Seight - segE)
            }
            check(SNine.length == 6)

            val otherSegG = Seight - Sfour - Sseven - segE
            check(segG == otherSegG)

            val (Sthree, segD) = letterCode.numbers.mapNotNull { letter ->
                val segment = letter - Sseven - segG
                if (segment.length == 1) {
                    return@mapNotNull Pair(letter, segment)
                }
                return@mapNotNull null
            }.single()

            val segB = Seight - Sthree - segE
            check(segB.length == 1)

            val Szero = letterCode.numbers.single {
                it == (Seight - segD)
            }
            check(Szero.length == 6)

            val (Ssix, segC) = letterCode.numbers.mapNotNull { letter ->
                if (listOf(Szero, SNine).contains(letter))
                    return@mapNotNull null
                val segment = Seight - letter
                if (segment.length == 1)
                    return@mapNotNull Pair(letter, segment)
                else
                    return@mapNotNull null
            }.single()
            check(segC.length == 1)
            check(Ssix.length == 6)

            val segF = Seight - segA - segB - segC - segD - segE - segG
            check(segF.length == 1)

            val Stwo = Seight - segB - segF
            val Sfive = Seight - segC - segE

            val res =  letterCode.fourOut.map { number ->
                when (number) {
                    Szero -> '0'
                    Sone -> '1'
                    Stwo -> '2'
                    Sthree -> '3'
                    Sfour -> '4'
                    Sfive -> '5'
                    Ssix -> '6'
                    Sseven -> '7'
                    Seight -> '8'
                    SNine -> '9'
                    else -> {
                        TODO("Unreachable!")}
                }
            }.joinToString("").toInt()
//            println("Row returns $res")
            return@map res
        }.sum().toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "26")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"387")

    val part2pretest = part2(listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"))
    println("----- PreTest result 2: $part2pretest")
    testResult(part2pretest, "5353")


    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "61229")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"986034")

    println("The time for part 1 and 2 without reading but with parsing (twice)")
    println("Time (ms):" + measureTimeMillis {
        part1(data)
        part2(data)
    })

}