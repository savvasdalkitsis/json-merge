/**
 * Copyright 2018 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.savvasdalkitsis.jsonmerger

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shazam.shazamcrest.MatcherAssert.assertThat
import com.shazam.shazamcrest.matcher.Matchers.sameBeanAs
import org.json.JSONObject
import org.junit.Test
import java.lang.IllegalArgumentException

class JsonMergerTest {

    @Test
    fun `overrides top level primitive values`() {
        val merged = JsonMerger().merge(baseJson = """
        {
            "param1": 1,
            "param1_override": 1,
            "param2": "2",
            "param2_override": "2",
            "param3": false,
            "param3_override": false
        }
        """, overrideJson = """
        {
            "param1_override": 2,
            "param2_override": "3",
            "param3_override": true
        }
        """)

        assertSameJson(merged, """
        {
            "param1": 1,
            "param1_override": 2,
            "param2": "2",
            "param2_override": "3",
            "param3": false,
            "param3_override": true
        }
        """)
    }

    @Test
    fun `merges array when instructed`() {
        val merged = JsonMerger(arrayMergeMode = JsonMerger.ArrayMergeMode.MERGE_ARRAY).merge(baseJson = """
        {
            "param": [1, 2, 3]
        }
        """, overrideJson = """
        {
            "param": [4, 5, 6]
        }
        """)

        assertSameJson(merged, """
        {
            "param": [1, 2, 3, 4, 5, 6]
        }
        """)
    }

    @Test
    fun `overrides array when instructed`() {
        val merged = JsonMerger(arrayMergeMode = JsonMerger.ArrayMergeMode.REPLACE_ARRAY).merge(baseJson = """
        {
            "param": [1, 2, 3]
        }
        """, overrideJson = """
        {
            "param": [4, 5, 6]
        }
        """)

        assertSameJson(merged, """
        {
            "param": [4, 5, 6]
        }
        """)
    }

    @Test
    fun `adds new parameters to base json`() {
        val merged = JsonMerger().merge(baseJson = """
        {
            "param1": 1
        }
        """, overrideJson = """
        {
            "param2": 2
        }
        """)

        assertSameJson(merged, """
        {
            "param1": 1,
            "param2": 2
        }
        """)
    }

    @Test
    fun `merges top level json arrays`() {
        val merged = JsonMerger(arrayMergeMode = JsonMerger.ArrayMergeMode.MERGE_ARRAY).merge("""
        [1, 2, 3]
        """, overrideJson = """
        [4, 5, 6]
        """)

        assertSameJson(merged, """
        [1, 2, 3, 4, 5, 6]
        """)
    }

    @Test
    fun `replaces top level json arrays`() {
        val merged = JsonMerger(arrayMergeMode = JsonMerger.ArrayMergeMode.REPLACE_ARRAY).merge("""
        [1, 2, 3]
        """, overrideJson = """
        [4, 5, 6]
        """)

        assertSameJson(merged, """
        [4, 5, 6]
        """)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `fails to merge inconsistent top level json elements`() {
        JsonMerger().merge(baseJson = """
        {}
        """, overrideJson = """
        []
        """)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `fails to merge top level primitive types`() {
        JsonMerger().merge(baseJson = """
        5
        """, overrideJson = """
        4
        """)
    }

    @Test
    fun `merges sub objects`() {
        val merged = JsonMerger().merge(baseJson = """
        {
            "param": {
                "p1": 1,
                "p1_override": 1
            }
        }
        """, overrideJson = """
        {
            "param": {
                "p1_override": 2
            }
        }
        """)

        assertSameJson(merged, """
        {
            "param": {
                "p1": 1,
                "p1_override": 2
            }
        }
        """)
    }

    @Test
    fun `replaces json objects when instructed`() {
        val merged = JsonMerger(objectMergeMode = JsonMerger.ObjectMergeMode.REPLACE_OBJECT).merge(baseJson = """
        {
            "keep": {
                "key": "value"
            },
            "param": {
                "key": "value"
            }
        }
        """, overrideJson = """
        {
            "param": {
                "newKey": "newValue"
            }
        }
        """)

        assertSameJson(merged, """
        {
            "keep": {
                "key": "value"
            },
            "param": {
                "newKey": "newValue"
            }
        }
        """)
    }

    @Test
    fun `merges JSONObjects to string`() {
        val merge = JsonMerger().mergeToString(baseJson = JSONObject("""
        {
            "param1": 1
        }
        """), overrideJson = JSONObject("""
        {
            "param2": 2
        }
        """))

        assertSameJson(merge, """
        {
            "param1": 1,
            "param2": 2
        }
        """)
    }

    @Test
    fun `merges JSONObjects to JSONObject`() {
        val merge = JsonMerger().merge(baseJson = JSONObject("""
        {
            "param1": 1
        }
        """), overrideJson = JSONObject("""
        {
            "param2": 2
        }
        """))

        assertSameJson(merge.toString(), """
        {
            "param1": 1,
            "param2": 2
        }
        """)
    }

    private fun assertSameJson(actual: String, expected: String) {
        assertThat(Gson().fromJson(actual, JsonElement::class.java), sameBeanAs(Gson().fromJson(expected, JsonElement::class.java)))
    }
}