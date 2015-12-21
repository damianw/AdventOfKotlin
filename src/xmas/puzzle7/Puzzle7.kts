package xmas.puzzle7

import java.io.File
import kotlin.text.MatchGroupCollection

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/19/15
 * (C) 2015 Damian Wieczorek
 */

interface Connection {

  fun computeValue(): Int

  class Literal(val value: Int): Connection {
    override fun computeValue() = value
    override fun toString() = value.toString()
  }

  class Not(val input: Connection): Connection {
    override fun computeValue() = input.computeValue().inv()
    override fun toString() = "NOT $input"
  }

  class Or(val one: Connection, val two: Connection): Connection {
    override fun computeValue() = one.computeValue() or two.computeValue()
    override fun toString() = "$one OR $two"
  }

  class And(val one: Connection, val two: Connection): Connection {
    override fun computeValue() = one.computeValue() and two.computeValue()
    override fun toString() = "$one AND $two"
  }

  class RightShift(val input: Connection, val bitCount: Int): Connection {
    override fun computeValue() = input.computeValue() shr bitCount
    override fun toString() = "$input RSHIFT $bitCount"
  }

  class LeftShift(val input: Connection, val bitCount: Int): Connection {
    override fun computeValue() = input.computeValue() shl bitCount
    override fun toString() = "$input LSHIFT $bitCount"
  }

  object Disconnected : Connection {
    override fun computeValue() = 0
    override fun toString() = "DISCONNECTED"
  }

}

class Wire(val name: String): Connection {

  var connection: Connection = Connection.Disconnected
    set(value) {
      field = value
      reset()
    }

  private var _value: Int? = null

  private val value: Int
    get() = _value ?: (connection.computeValue() and 0x0000FFFF).apply { _value = this }

  fun reset() {
    _value = null
  }

  override fun computeValue() = value

  override fun toString() = "$name: $value"

  fun deepToString() = "$connection -> $name"

}

object Circuit {

  val wires = hashMapOf<String, Wire>()

  fun reset() = wires.values.forEach(Wire::reset)

  operator fun get(wireName: String) = wires[wireName]
      ?: Wire(wireName).apply { wires[name] = this }

  override fun toString() = wires.values.sortedBy { it.name }.joinToString("\n") { it.toString() }

}

object Parser {

  private val unaryOperation = """(NOT )?([a-z\d]+) -> ([a-z]+)""".toRegex()
  private val binaryOperation = """([a-z\d]+) (OR|AND|LSHIFT|RSHIFT) ([a-z\d]+) -> ([a-z]+)""".toRegex()

  private fun parseConnection(input: String) = try {
    Connection.Literal(input.toInt())
  } catch (e: NumberFormatException) {
    Circuit[input]
  }

  private fun parseUnary(groups: MatchGroupCollection) = parseConnection(groups[2]!!.value).let {
    if (groups[1]?.value.isNullOrEmpty()) it else Connection.Not(it)
  }

  private fun parseBinary(groups: MatchGroupCollection): Connection {
    val one = parseConnection(groups[1]!!.value)
    val two = parseConnection(groups[3]!!.value)
    val operator = groups[2]!!.value
    return when (operator) {
      "OR" -> Connection.Or(one, two)
      "AND" -> Connection.And(one, two)
      "LSHIFT" -> Connection.LeftShift(one, (two as Connection.Literal).value)
      "RSHIFT" -> Connection.RightShift(one, (two as Connection.Literal).value)
      else -> throw AssertionError()
    }
  }

  fun parse(line: String): Wire {
    val groups = unaryOperation.matchEntire(line)?.groups
        ?: binaryOperation.matchEntire(line)!!.groups
    val connection = if (groups.size == 4) parseUnary(groups) else parseBinary(groups)
    val target = Circuit[groups.last()!!.value]
    return target.apply {
      this.connection = connection
    }
  }

}

File("./input.txt").forEachLine { Parser.parse(it) }

println("Part 1:")
println(Circuit)

Circuit["b"].connection = Connection.Literal(Circuit["a"].computeValue())
Circuit.reset()

println("\nPart 2:")
println(Circuit)
