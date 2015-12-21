package xmas.puzzle5

import java.io.File

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/18/15
 * (C) 2015 Damian Wieczorek
 */

fun <T> Sequence<T>.overlappingPairwise() = iterator().let { iterator ->
  if (!iterator.hasNext()) return emptySequence<Pair<T, T>>()
  var previous = iterator.next()
  sequence {
    if (!iterator.hasNext()) null
    else Pair(previous, iterator.next()).apply { previous = second }
  }
}

val vowels = setOf('a', 'e', 'i', 'o', 'u')

val illegalPairs = listOf("ab", "cd", "pq", "xy")

fun String.hasAtLeastThreeVowels() = vowels.sumBy { vowel -> count { it == vowel } } >= 3

fun String.hasDoubleLetter() = asSequence().overlappingPairwise().any { it.first == it.second }

fun String.containsIllegalPairs() = illegalPairs.any { it in this }

fun String.isNice() = hasAtLeastThreeVowels() && hasDoubleLetter() && !containsIllegalPairs()

val INPUT = File("./input.txt").readLines()

val result = INPUT.count { it.isNice() }

println("Nice words: $result")
