package xmas.puzzle3

import java.awt.Point
import java.io.File

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/18/15
 * (C) 2015 Damian Wieczorek
 */

object Santa {

  private val position = Point()
  val visited = hashSetOf<Point>()

  fun save() = visited.add(Point(position))

  fun left() = save().let { position.x -= 1 }
  fun up() = save().let { position.y += 1 }
  fun right() = save().let { position.x += 1 }
  fun down() = save().let { position.y -= 1 }

}

val INPUT = File("./input.txt").readText().trim()

INPUT.forEach {
  when (it) {
    '<' -> Santa.left()
    '^' -> Santa.up()
    '>' -> Santa.right()
    'v' -> Santa.down()
    else -> throw AssertionError()
  }
}

println("Houses visited: ${Santa.visited.size}")
