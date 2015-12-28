package xmas.puzzle16

import java.io.File
import kotlin.reflect.memberProperties
import kotlin.reflect.primaryConstructor

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/28/15
 * (C) 2015 Damian Wieczorek
 *
 * This is a standard Kotlin file (rather than a script) to enable reflection because I wanted to use reflection
 */
data class AuntSue(
    val children: Int? = null,
    val cats: Int? = null,
    val samoyeds: Int? = null,
    val pomeranians: Int? = null,
    val akitas: Int? = null,
    val vizslas: Int? = null,
    val goldfish: Int? = null,
    val trees: Int? = null,
    val cars: Int? = null,
    val perfumes: Int? = null) {

  companion object {
    private val primaryConstructor = AuntSue::class.primaryConstructor!!
    private val parameters = primaryConstructor.parameters.toMapBy { it.name }
    operator fun invoke(arguments: Map<String, Int>) = primaryConstructor.callBy(arguments.mapKeys { parameters[it.key]!! })
  }
}

val CHALLENGE = AuntSue(
    children = 3,
    cats = 7,
    samoyeds = 2,
    pomeranians = 3,
    akitas = 0,
    vizslas = 0,
    goldfish = 5,
    trees = 3,
    cars = 2,
    perfumes = 1
)

val REGEX = """([a-z]+): (\d+)""".toRegex()

val AUNTS = File("./input.txt").readLines().map { line ->
  val auntProperties = REGEX.findAll(line).map {
    it.groups.drop(1).map {
      it!!.value
    }.let {
      it[0] to it[1].toInt()
    }
  }.toMap()
  AuntSue(auntProperties)
}

fun main(args: Array<String>) {
  val properties = AuntSue::class.memberProperties

  val partOne = AUNTS.indexOfFirst { sue ->
    properties.all { property ->
      val expected = property.get(CHALLENGE)
      val actual = property.get(sue)
      actual == null || actual == expected
    }
  } + 1
  println("Part 1: $partOne")

  val partTwo = AUNTS.indexOfFirst { sue ->
    properties.all { property ->
      val expected = property.get(CHALLENGE) as Int
      val actual = property.get(sue) as Int?
      actual == null || when (property.name) {
        "cats", "trees" -> actual > expected
        "pomeranians", "goldfish" -> actual < expected
        else -> actual == expected
      }
    }
  } + 1
  println("Part 2: $partTwo")
}
