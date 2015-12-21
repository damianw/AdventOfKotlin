package xmas.puzzle5

import java.io.File

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/18/15
 * (C) 2015 Damian Wieczorek
 */
data class IndexedPair<T>(val first: T, val second: T, val startingIndex: Int) {
  val range = startingIndex..startingIndex + 1
}

fun IntRange.overlaps(other: IntRange) = other.start in this || other.endInclusive in this

fun <T> Sequence<T>.overlappingPairwise() = iterator().let { iterator ->
  if (!iterator.hasNext()) return emptySequence<IndexedPair<T>>()
  var position = 0
  var previous = iterator.next()
  sequence {
    if (!iterator.hasNext()) null
    else IndexedPair(previous, iterator.next(), position++).apply { previous = second }
  }
}

fun <T> Sequence<T>.gappedPairwise() = overlappingPairwise().overlappingPairwise().map {
  Pair(it.first.first, it.second.second)
}

fun String.hasDoublePair() = asSequence().overlappingPairwise().toList().let { pairs ->
  pairs.any { pair ->
    pairs.any { it.first == pair.first && it.second == pair.second && !pair.range.overlaps(it.range) }
  }
}

fun String.hasRepeatedLetterWithGap() = asSequence().gappedPairwise().any { it.first == it.second }

fun String.isNice() = hasDoublePair() && hasRepeatedLetterWithGap()

val INPUT = File("./input.txt").readLines()

val result = INPUT.count { it.isNice() }

println("Nice words: $result")
