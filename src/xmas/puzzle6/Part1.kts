package xmas.puzzle6

import java.awt.Point
import java.io.File


/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/18/15
 * (C) 2015 Damian Wieczorek
 */

data class Rect(val bottomLeft: Point, val topRight: Point) {
  val width = bottomLeft.x..topRight.x
  val height = bottomLeft.y..topRight.y
  val points = width.flatMap { x -> height.map { y -> Point(x, y) } }
}

class Grid(val size: Int) {

  val lights = BooleanArray(size * size)

  operator fun get(point: Point) = lights[point.x * size + point.y]

  operator fun set(point: Point, value: Boolean) {
    lights[point.x * size + point.y] = value
  }

}

sealed class Instruction(val rect: Rect) {

  fun applyTo(grid: Grid) {
    for (point in rect.points) {
      applyTo(grid, point)
    }
  }

  abstract fun applyTo(grid: Grid, point: Point)

  class On(rect: Rect) : Instruction(rect) {
    override fun applyTo(grid: Grid, point: Point) {
      grid[point] = true
    }
  }

  class Off(rect: Rect) : Instruction(rect) {
    override fun applyTo(grid: Grid, point: Point) {
      grid[point] = false
    }
  }

  class Toggle(rect: Rect) : Instruction(rect) {
    override fun applyTo(grid: Grid, point: Point) {
      grid[point] = !grid[point]
    }
  }

  companion object {
    val regex = """(turn (?:on|off)|toggle) (\d+),(\d+) through (\d+),(\d+)""".toRegex()

    fun parse(input: String): Instruction {
      val match = regex.matchEntire(input)!!
      val groups = match.groups
      val action = groups[1]!!.value
      val left = groups[2]!!.value.toInt()
      val bottom = groups[3]!!.value.toInt()
      val right = groups[4]!!.value.toInt()
      val top = groups[5]!!.value.toInt()
      val rect = Rect(Point(left, bottom), Point(right, top))
      return when (action) {
        "turn on" -> On(rect)
        "turn off" -> Off(rect)
        "toggle" -> Toggle(rect)
        else -> throw AssertionError()
      }
    }

  }

}

val INPUT = File("./input.txt").readLines().map { Instruction.parse(it) }

val grid = Grid(1000)

INPUT.forEach {
  it.applyTo(grid)
}

println(grid.lights.count { it })
