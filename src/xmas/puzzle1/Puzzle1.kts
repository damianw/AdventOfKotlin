package xmas.puzzle1

import java.io.File

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/18/15
 * (C) 2015 Damian Wieczorek
 */

val INPUT = File("./input.txt").readText().trim()

fun String.floor() = count { it == '(' } - count { it == ')' }

fun String.floorAt(index: Int) = this.substring(0..index).run { floor() }

val result = (0 until INPUT.length).find { INPUT.floorAt(it) == -1 }?.let { it + 1 }

println(result)
