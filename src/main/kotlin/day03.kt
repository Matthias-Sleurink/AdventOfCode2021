@file:Suppress("DuplicatedCode")

fun main() {
    fun mostcommonchar(index: Int, list: List<String>): Char {
        val map = HashMap<Char, Int>().withDefault { 0 }
        list.forEach { map[it[index]] = (map.getValue(it[index]) + 1) }

        if (map.getValue('0') > map.getValue('1'))
            return '0'
        return '1'

    }

    val day = 3
    fun parseInput(input: List<String>): Sequence<String> {
        return input.asSequence()
    }

    fun part1(inputLines: List<String>): String {
        val indexCounters = MutableList(inputLines[0].length) {0}
        parseInput(inputLines)
            .forEach {
                it.mapIndexed { index, char ->
                    indexCounters.set(index, indexCounters[index] + if (char == '0') -1 else 1)
                }
            }
        var gamma = ""
        var epsilon = ""
        indexCounters.forEach { value ->
            if (value < 0) { // most common: 0
                gamma += "0"
                epsilon += "1"
            } else if (value > 0) { // most common: 1
                gamma += "1"
                epsilon += "0"
            } else {
                TODO()
            }
        }
        val gammaNumber = Integer.parseInt(gamma, 2)
        val epsilonNumber = Integer.parseInt(epsilon, 2)
        return (gammaNumber * epsilonNumber).toString()
    }

    fun part2(inputLines: List<String>): String {
        var listOxy = parseInput(inputLines).toList()
        var listCO2 = parseInput(inputLines).toList()
        repeat(inputLines[0].length) { index ->
            if (listOxy.size > 1) {
                val mostCommonOxy = mostcommonchar(index, listOxy)
                listOxy = listOxy.filter { it[index] ==  mostCommonOxy}
            }
            if (listCO2.size > 1) {
                val mostCommonC02 = mostcommonchar(index, listCO2)
                listCO2 = listCO2.filterNot { it[index] == mostCommonC02 }
            }
        }
        val oxygen = Integer.parseInt(listOxy.first(), 2)
        val co2 = Integer.parseInt(listCO2.first(), 2)

        return (oxygen * co2).toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "198")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"1131506")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "230")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"7863147")
}