package xmas.puzzle10

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/22/15
 * (C) 2015 Damian Wieczorek
 */
fun String.droppingFirstGroup() = firstOrNull()?.let { first ->
  dropWhile { it == first }
} ?: this

fun String.lookAndSay(): String = StringBuilder().let {
  this.lookAndSay(it)
  it.toString()
}

tailrec fun String.lookAndSay(stringBuilder: StringBuilder) {
  if (isEmpty()) return
  val tail = droppingFirstGroup()
  stringBuilder.append(length - tail.length)
  stringBuilder.append(first())
  tail.lookAndSay(stringBuilder)
}

fun String.lookAndSay(iterations: Int) = sequence(lookAndSay()) { it.lookAndSay() }.take(iterations).last()

val INPUT = "1113222113"

inline fun time(block: () -> Unit) {
  println("--> Begin")
  val startTime = System.currentTimeMillis()
  block()
  val endTime = System.currentTimeMillis()
  val duration = endTime - startTime
  println("--> Time: $duration ms")
}

time {
  println("Part 1: ${INPUT.lookAndSay(40).length}")
}

time {
  println("Part 2: ${INPUT.lookAndSay(50).length}")
}
