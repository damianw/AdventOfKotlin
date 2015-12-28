package xmas.puzzle12

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.io.File

/**
 * @author Damian Wieczorek {@literal <damian@farmlogs.com>}
 * @since 12/23/15
 * (C) 2015 Damian Wieczorek
 */
val json = File("./input.txt").reader().use {
  Gson().fromJson<JsonObject>(it)
}

fun JsonObject.values() = entrySet().map { it.value }

fun JsonElement.sum(): Int = when {
  isJsonPrimitive -> asJsonPrimitive.run {
    if (isNumber) asInt else 0
  }
  isJsonObject -> asJsonObject.values().sumBy { it.sum() }
  isJsonArray -> asJsonArray.sumBy { it.sum() }
  else -> 0
}

println(json.sum())
