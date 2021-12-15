import java.io.File
import java.util.*
import kotlin.NoSuchElementException

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

fun <T> MutableCollection<T>.removeFirst(): T {
    if (size == 0)
        throw NoSuchElementException("Cannot remove first from empty list.")

    return with(iterator()){ next().also{ remove() }}
}

fun List<Long>.median(): Long {
    return this.sorted()[this.size / 2]
}
fun List<Int>.median(): Int {
    return this.sorted()[this.size / 2]
}

infix fun <A,B> A.pairWith(that: B): Pair<A, B> = Pair(this, that)

fun String.isLowercase(): Boolean {
    return this.all { it.isLowerCase() }
}

fun String.isUppercase(): Boolean {
    return this.all { it.isUpperCase() }
}

fun <A> stackOf(head: A): Stack<A> = Stack<A>().also { it.push(head) }

fun String.toIntPair(splitter: String = ","): Pair<Int, Int> {
    return this.split(splitter).let { (left, right) -> left.toInt() pairWith right.toInt()}
}

fun <K, V> Iterable<Pair<K, V>>.toMutableMap(): MutableMap<K, V> = this.toMap().toMutableMap()

fun <E> List<E>.toPair(): Pair<E, E> {
    check(this.count() == 2)
    return this.first() pairWith this.second()
}

fun String.second(): Char = this.drop(1).first()

fun <E> List<E>.second(): E = this.component2()
fun <E> List<E>.third(): E = this.component3()
fun <E> List<E>.fourth(): E = this.component4()
fun <E> List<E>.fifth(): E = this.component5()

/**
 * Returns the second last element.
 *
 * @throws NoSuchElementException if the list is empty or has 1 element.
 */
fun <T> List<T>.secondLast(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    if (lastIndex == 0)
        throw NoSuchElementException("List has no second last.")

    return this[lastIndex-1]
}


// This is supposed to act like it is a constructor, so we name it like a class
@Suppress("FunctionName")
fun <E> IntCounter(): MutableMap<E, Int> = mutableMapOf<E, Int>().withDefault { 0 }

// This is supposed to act like it is a constructor, so we name it like a class
@Suppress("FunctionName")
fun <E> Counter(): MutableMap<E, Long> = mutableMapOf<E, Long>().withDefault { 0 }
