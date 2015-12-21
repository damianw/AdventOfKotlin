package xmas.puzzle3

import java.awt.Point
import java.io.File

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/18/15
 * (C) 2015 Damian Wieczorek
 */

class Santa {

  private val position = Point()
  val visited = hashSetOf<Point>()

  fun save() = visited.add(Point(position))

  fun left() = save().let { position.x -= 1 }
  fun up() = save().let { position.y += 1 }
  fun right() = save().let { position.x += 1 }
  fun down() = save().let { position.y -= 1 }

  fun deliverPresents(movements: List<Char>) = movements.forEach {
    when (it) {
      '<' -> left()
      '^' -> up()
      '>' -> right()
      'v' -> down()
      else -> throw AssertionError()
    }
    save()
  }

}

val INPUT = File("./input.txt").readText().trim().toList()

val inputs  = INPUT.withIndex().partition { it.index % 2 == 0 }
val santaInput = inputs.first.map { it.value }
val roboInput = inputs.second.map { it.value }

val santa = Santa()
val roboSanta = Santa()

santa.deliverPresents(santaInput)
roboSanta.deliverPresents(roboInput)

val totalVisited = santa.visited + roboSanta.visited

println("Houses visited: ${totalVisited.size}")
