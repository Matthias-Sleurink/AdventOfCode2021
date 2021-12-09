@file:Suppress("DuplicatedCode")

fun main() {
    val day = 6

    // Map of age -> count
    class FishState(var state: Map<Int, Long>) {
        fun ageDays(n:Int) {
            repeat(n) {this.ageDay()}
        }

        fun ageDay() {
            val newState = mutableMapOf<Int, Long>()

            state.forEach { (age, count) ->
                newState[age-1] = count
            }

            val newCount = newState.getOrDefault(-1, 0)
            newState.remove(-1)
            newState[8] = newCount
            newState[6] = newState.getOrDefault(6, 0) + newCount

            state = newState
        }
    }

    fun parseInput(input: List<String>): FishState {
        val state = mutableMapOf<Int, Long>()
        input[0].split(",").map { it.toInt() }.forEach {
            state[it] = state.getOrDefault(it, 0) + 1L
        }
        return FishState(state)
    }

    fun part1(inputLines: List<String>): String {
        val state = parseInput(inputLines)
        state.ageDays(80)
        return state.state.values.sum().toString()
    }

    fun part2(inputLines: List<String>): String {
        val state = parseInput(inputLines)
        state.ageDays(256)
        return state.state.values.sum().toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "5934")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"360610")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "26984457539")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"1631629590423")





}