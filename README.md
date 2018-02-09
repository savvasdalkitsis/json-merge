json-merge
======

**json-merge** is a library allowing you to merge two json files for the JVM.

It currently supports two modes for merging arrays and objects.


### Merging primitive values

**json-merge** will merge all keys inside Json objects (in the default mode)
and for keys that are present in both the base and override json, it will 
simply use the ones from the override:

- base

```json
{
  "age": 24,
  "name": "John",
  "registered": false
}
```

- override

```json
{
  "lastName": "Doe",
  "age": 30,
  "registered": true
}
```

- merged

```json
{
  "age": 30,
  "name": "John",
  "lastName": "Doe",
  "registered": true
}
```

### Merging arrays

There are two modes for merging arrays:

#### REPLACE_ARRAY

```kotlin
JsonMerger(arrayMergeMode = JsonMerger.ArrayMergeMode.REPLACE_ARRAY)
```

- base

```json
{
  "array": [1, 2, 3]
}
```

- override

```json
{
  "array": [4, 5, 6]
}
```

- merged

```json
{
  "array": [4, 5, 6]
}
```

#### MERGE_ARRAY

```kotlin
JsonMerger(arrayMergeMode = JsonMerger.ArrayMergeMode.MERGE_ARRAY)
```

- base

```json
{
  "array": [1, 2, 3]
}
```
- override

```json
{
  "array": [4, 5, 6]
}
```

- merged

```json
{
  "array": [1, 2, 3, 4, 5, 6]
}
```

### Merging objects

There are two modes for merging objects:

#### REPLACE_OBJECT

```kotlin
JsonMerger(objectMergeMode = JsonMerger.ObjectMergeMode.REPLACE_OBJECT)
```

- base

```json
{
  "object": {
    "param1": true
  }
}
```

- override

```json
{
  "object": {
    "param2": true
  }
}
```

- merged

```json
{
  "object": {
    "param2": true
  }
}
```

#### MERGE_OBJECT

```kotlin
JsonMerger(objectMergeMode = JsonMerger.ObjectMergeMode.MERGE_OBJECT)
```

- base

```json
{
  "object": {
    "param1": true
  }
}
```

- override
```json
{
  "object": {
    "param2": true
  }
}
```

- merged

```json
{
  "object": {
      "param1": true,
      "param2": true
  }
}
```

License
-------

    Copyright 2019 Savvas Dalkitsis

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.