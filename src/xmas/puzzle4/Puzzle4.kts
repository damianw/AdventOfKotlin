package xmas.puzzle4

import java.math.BigInteger
import java.security.MessageDigest

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/18/15
 * (C) 2015 Damian Wieczorek
 */
fun String.md5() = BigInteger(1, digest.digest(this.toByteArray())).toString(16)

val INPUT = "bgvyzdsv"
val digest = MessageDigest.getInstance("MD5")

val sequence = (1..Int.MAX_VALUE).asSequence().map { INPUT + it }

val part1 = sequence.find { it.md5().length < 28 }
println("Part 1: $part1")

val part2 = sequence.find { it.md5().length < 27 }
println("Part 2: $part2")
