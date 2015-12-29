package xmas.puzzle17

import java.io.File

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/28/15
 * (C) 2015 Damian Wieczorek
 *
 * Linear search is slow but works for a data set this small. Normally I would use some kind of
 * sorted data structure to solve the problem in a less naive way by eliminating impossible combinations.
 */
fun <T> List<T>.combinations() = (0..size).flatMap { asSequence().combinations(it).asIterable() }

fun <T> Sequence<T>.combinations(k: Int): Sequence<Sequence<T>> = if (k == 0) sequenceOf(emptySequence()) else withIndex().flatMap { e ->
  drop(e.index + 1).combinations(k - 1).map { sequenceOf(e.value) + it }
}

val containers = File("./input.txt").readLines().map { it.toInt() }

val target = 150

val possibilities = containers.combinations().filter { it.sum() == target }.map { it.toList() }.toList()

println("Part 1: ${possibilities.size}")

val minContainers = possibilities.map { it.size }.min()

val part2 = possibilities.count { it.size == minContainers }

println("Part 2: $part2")
