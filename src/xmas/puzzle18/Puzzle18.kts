package xmas.puzzle18

import java.awt.Point
import java.io.File

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/29/15
 * (C) 2015 Damian Wieczorek
 */
class Grid(val size: Int = 100) {

  val lights = BooleanArray(size * size)

  val corners = listOf(Point(0, 0), Point(size - 1, 0), Point(0, size - 1), Point(size - 1, size - 1))

  operator fun get(x: Int, y: Int) = lights[x * size + y]

  operator fun set(x: Int, y: Int, value: Boolean) {
    lights[x * size + y] = value
  }

  operator fun get(point: Point) = get(point.x, point.y)

  operator fun set(point: Point, value: Boolean) = set(point.x, point.y, value)

  fun lightCorners() = corners.forEach {
    this[it] = true
  }

  fun pointSequence() = (0 until size).asSequence().flatMap { x ->
    (0 until size).asSequence().map { y ->
      Point(x, y)
    }
  }

  fun toStepSequence(cornersOn: Boolean = false) = sequence(this) { previous ->
    Grid(previous.size).apply {
      previous.pointSequence().forEach { point ->
        this@apply[point] = previous.nextStateOf(point)
      }
      if (cornersOn) lightCorners()
    }
  }

  fun computeStateAfter(steps: Int, cornersOn: Boolean = false) = toStepSequence(cornersOn).drop(steps).first()

  fun neighboringRangeOf(i: Int) = ((i - 1).coerceAtLeast(0)..(i + 1).coerceAtMost(size - 1)).asSequence()

  fun neighborsOf(point: Point) = neighboringRangeOf(point.x).flatMap { x ->
    neighboringRangeOf(point.y).map { y ->
      Point(x, y)
    }
  }.filterNot { it == point }

  fun nextStateOf(point: Point) = neighborsOf(point).count { this[it] }.let { it == 3 || it == 2 && this[point] }

  override fun toString() = lights.joinToString()

}

val INPUT = Grid()

File("./input.txt").readLines().forEachIndexed { x, row ->
  row.forEachIndexed { y, light ->
    INPUT[x, y] = light == '#'
  }
}

val part1 = INPUT.computeStateAfter(100)

println("Part 1: ${part1.lights.count { it }}")

INPUT.lightCorners()

val part2 = INPUT.computeStateAfter(100, cornersOn = true)

println("Part 2: ${part2.lights.count { it }}")
