package com.savvasdalkitsis.jsonmerger

import com.savvasdalkitsis.jsonmerger.JsonMerger.ArrayMergeMode.MERGE_ARRAY
import com.savvasdalkitsis.jsonmerger.JsonMerger.ArrayMergeMode.REPLACE_ARRAY
import com.savvasdalkitsis.jsonmerger.JsonMerger.ObjectMergeMode.MERGE_OBJECT
import com.savvasdalkitsis.jsonmerger.JsonMerger.ObjectMergeMode.REPLACE_OBJECT
import org.json.JSONArray
import org.json.JSONObject

class JsonMerger(val arrayMergeMode: ArrayMergeMode = REPLACE_ARRAY,
                 val objectMergeMode: ObjectMergeMode = MERGE_OBJECT) {

    enum class ArrayMergeMode {
        REPLACE_ARRAY,
        MERGE_ARRAY
    }

    enum class ObjectMergeMode {
        REPLACE_OBJECT,
        MERGE_OBJECT
    }

    fun merge(baseJson: String, overrideJson: String): String = try {
        mergeElement(parse(baseJson), parse(overrideJson)).toString()
    } catch (e: Exception) {
        throw throwUnsupportedElements(baseJson, overrideJson, e)
    }

    private fun mergeElement(base: Any?, newValue: Any) = when (base) {
        is JSONObject -> mergeObject(base, newValue)
        is JSONArray -> mergeArray(base, newValue)
        else -> newValue
    }

    private fun mergeObject(base: JSONObject, override: Any): JSONObject {
        if (override !is JSONObject) {
            throw IllegalArgumentException(msg(base, override))
        }
        override.keys().forEach { key ->
            base[key] = when (objectMergeMode) {
                REPLACE_OBJECT -> override[key]
                MERGE_OBJECT -> mergeElement(base.valueOrNull(key), override[key])
            }
        }
        return base
    }

    private fun mergeArray(base: JSONArray, override: Any): JSONArray {
        if (override !is JSONArray) {
            throw IllegalArgumentException(msg(base, override))
        }
        return when (arrayMergeMode) {
            REPLACE_ARRAY -> override
            MERGE_ARRAY -> base.apply { override.forEach { put(it) } }
        }
    }

    private fun parse(json: String) = try {
        JSONObject(json)
    } catch (_: Throwable) {
        JSONArray(json)
    }

    private operator fun JSONObject.set(key: String, value: Any) {
        put(key, value)
    }

    private fun msg(base: Any, override: Any) =
            "Trying to merge two elements of different type. Base type was ${base::class} and override was ${override::class}"

    private fun throwUnsupportedElements(baseJson: String, overrideJson: String, cause: Exception) =
            IllegalArgumentException("Can only merge json objects or arrays. Base json was '$baseJson', override was '$overrideJson'", cause)

}

private fun JSONObject.valueOrNull(key: String) = if (has(key)) this[key] else null