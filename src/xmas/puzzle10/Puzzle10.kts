package xmas.puzzle10

import kotlin.jvm.internal.iterator

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/22/15
 * (C) 2015 Damian Wieczorek
 */
val INPUT = "113333222113"

data class CountedValue<T>(val value: T, val count: Int)

class AppendingIterator<T>(val first: T, val rest: Iterator<T>): Iterator<T> {
  private var takenFirst = false
  override fun hasNext() = if (!takenFirst) true else rest.hasNext()
  override fun next() = if (!takenFirst) first.apply { takenFirst = true } else rest.next()
}

inline fun <T> Iterator<T>.countWhile(predicate: (T) -> Boolean): Int {
  var count = 0
  for (element in this) {
    if (!predicate(element)) break
    count += 1
  }
  return count
}

fun <T> Iterator<T>.countWhileEqual(): Pair<CountedValue<T>, Iterator<T>>? {
  val initial = if (hasNext()) next() else return null
  var previous = initial
  val count = countWhile { (it == previous).apply { previous = it } } + 1
  val nextIterator = if (previous != initial) AppendingIterator(previous, this) else this
  return Pair(CountedValue(initial, count), nextIterator)
}

fun <T> Iterator<T>.toCountingSequence(): Sequence<CountedValue<T>> =
    sequence(countWhileEqual()) { it.second.countWhileEqual() }.map { it.first }

fun String.lookAndSay() = iterator().toCountingSequence().map { "${it.count}${it.value}" }.joinToString("").apply {
  println(this)
}

fun String.lookAndSay(iterations: Int) = sequence(lookAndSay()) { it.lookAndSay() }.take(iterations).last()

println(INPUT.lookAndSay(40))
