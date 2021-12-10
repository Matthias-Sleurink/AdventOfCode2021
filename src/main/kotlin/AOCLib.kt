import java.io.File

fun readFileForDay(day: Int): List<String> {
    val name = day.toString().padStart(2, '0')
    return File("data/$name.aoc").readLines()
}

fun readTestFileForDay(day: Int): List<String> {
    val name = day.toString().padStart(2, '0')
    return File("data/$name.test.aoc").readLines()
}

fun testResult(result: String, expected: String, prompt:String = "Test result okay:") {
    if (result == expected) {
        println("$prompt result == Expected $expected")
        return
    }
    System.err.println("Result != Expected!")
    printDiff(result, expected)

    throw IllegalStateException("Result != Expected!")
}

fun printDiff(result: String, expected: String) {
    println("Expected: $expected")
    println("Got     : $result")
}

fun testFinalResult(result: String, expected: String?) {
    if (expected == null) return

    testResult(result, expected, "re-verification of final result okay:")
}

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

fun <E> List<List<E>>.transpose(): List<List<E>> {
    // Helpers
    fun <E> List<E>.head(): E = this.first()
    fun <E> List<E>.tail(): List<E> = this.takeLast(this.size - 1)
    fun <E> E.append(xs: List<E>): List<E> = listOf(this).plus(xs)

    this.filter { it.isNotEmpty() }.let { ys ->
        return when (ys.isNotEmpty()) {
            true -> ys.map { it.head() }.append(ys.map { it.tail() }.transpose())
            else -> emptyList()
        }
    }
}

fun String.sorted(): String {
    return this.toList().sorted().joinToString("")
}

operator fun String.minus(other: String): String {
    return this.filterNot { other.contains(it) }
}

fun <T> MutableCollection<T>.removeFirst()
        = with(iterator()){ next().also{ remove() }}

fun List<Long>.median(): Long {
    return this.sorted()[this.size / 2]
}
fun List<Int>.median(): Int {
    return this.sorted()[this.size / 2]
}


// This is supposed to act like it is a constructor, so we name it like a class
@Suppress("FunctionName")
fun <E> Counter(): MutableMap<E, Int> = mutableMapOf<E, Int>().withDefault { 0 }
