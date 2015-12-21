package xmas.puzzle2

import java.io.File

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/18/15
 * (C) 2015 Damian Wieczorek
 */

val boxes = File("./input.txt").readLines().map { line ->
  line.split('x').map(String::toInt).let { dimensions ->
    val (l, w, h) = dimensions
    Box(l, w, h)
  }
}

data class Box(val l: Int, val w: Int, val h: Int) {
  val dimensions = listOf(l, w, h)
  val sides = listOf(l * w, w * h, h * l)
  val surfaceArea = 2 * sides.sum()
  val paperRequired = surfaceArea + sides.min()!!
  val minPerimeter = 2 * (dimensions - dimensions.max()!!).sum()
  val volume = l * w * h
  val ribbonRequired = minPerimeter + volume
}

val paperRequired = boxes.sumBy { it.paperRequired }

println("Paper required: $paperRequired")

val ribbonRequired = boxes.sumBy { it.ribbonRequired }

println("Ribbon required: $ribbonRequired")
