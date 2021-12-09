@file:Suppress("DuplicatedCode")


fun main() {
    val day = 4

    fun List<List<Pair<Int, Boolean>>>.hasWonBingoRowWise(): Boolean {
        return this.any{row -> row.all {it.second}}
    }

    // each element in a row holds if it has been marked
    class Board(var rows: List<List<Pair<Int, Boolean>>>) {
        override fun toString(): String {
            return "Board: $rows"
        }

        // returns if this means that the lad won
        fun draw(number: Int): Boolean {
            rows = rows.map { row ->
                row.map { element ->
                    if (element.first == number) {
                        Pair(number, true)
                    }
                    else {
                        element
                    }
                }
            }
            // check if there is any row for which all numbers are drawn
            return rows.hasWonBingoRowWise() or
                    rows.transpose().hasWonBingoRowWise()
        }

        fun sumUnmarked(): Int {
            return rows.sumOf { row ->
                row.sumOf { element -> if (element.second) 0 else element.first }
            }
        }
    }
    class Game(val numbers: List<Int>, var boards: List<Board>) {
        override fun toString(): String {
            return "Game: Numbers: $numbers,\nboards: $boards"
        }

        fun getFirstWinningBoard(): Pair<Board, Int> {
            numbers.forEach { number ->
                boards.forEach{board -> if (board.draw(number)) return Pair(board, number) }
            }
            throw IllegalStateException("No board won in this game! $this")
        }

        // This feels like it could be a fold?
        fun getLastWinningBoard(): Pair<Board, Int> {
            var lastWinning = Pair(Board(listOf()), 0)
            numbers.forEach { number ->
                boards = boards.mapNotNull { board ->
                    if (board.draw(number)) {
                        lastWinning = Pair(board, number)
                        null
                    } else {
                        board
                    }
                }
            }
            return lastWinning
        }
    }

    fun parseInput(input: List<String>): Game {
        val numbers = input[0].trim().split(",").map { Integer.parseInt(it) }

        // board is list of rows
        val boards = input.asSequence()
            .drop(1).chunked(6).map { it.drop(1) } // chunks of 5 rows
            .map { it.map{ it1 -> it1.split(" ").filterNot { it2 -> it2 == "" }.map{ it2 -> Pair(Integer.parseInt(it2), false)}} }
            .map{Board(it)}
            .toList()
        return Game(numbers, boards)
    }

    fun part1(inputLines: List<String>): String {
        val (board, lastNumber) = parseInput(inputLines).getFirstWinningBoard()
        return (board.sumUnmarked() * lastNumber).toString()
    }

    fun part2(inputLines: List<String>): String {
        val (board, lastNumber) = parseInput(inputLines).getLastWinningBoard()
        return (board.sumUnmarked() * lastNumber).toString()
    }

    val testData = readTestFileForDay(day)
    val part1test = part1(testData)
    println("----- Test result 1: $part1test")
    testResult(part1test, "4512")

    val data = readFileForDay(day)
    val part1res = part1(data)
    println("----- Result 1: $part1res")
    testFinalResult(part1res,"39984")

    val part2test = part2(testData)
    println("----- Test result 2: $part2test")
    testResult(part2test, "1924")

    val part2res = part2(data)
    println("----- Result 2: $part2res")
    testFinalResult(part2res,"8468")
}