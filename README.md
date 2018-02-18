[![CircleCI](https://circleci.com/gh/savvasdalkitsis/json-merge.svg?style=shield)](https://circleci.com/gh/savvasdalkitsis/json-merge)

json-merge
======

**json-merge** is a library allowing you to merge two json files for the JVM written in Kotlin.

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

##### REPLACE_ARRAY

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

##### MERGE_ARRAY

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

##### REPLACE_OBJECT

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

##### MERGE_OBJECT

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

### Overriding global object merge mode

It is possible to change the object merge mode for specific json objects in the document by
using a special annotation key `__json-merge:objectMergeMode` with two possible values
`replaceObject` and `mergeObject`:


```kotlin
JsonMerger(objectMergeMode = JsonMerger.ObjectMergeMode.MERGE_OBJECT)
```

- base

```json
{
  "object1": {
    "param1": true
  },
  "object2": {
    "param2": true
  },
}
```

- override

```json
{
  "object1": {
    "param2": true
  },
  "object2": {
    "__json-merge:objectMergeMode": "replaceObject",
    "param3": true
  },
}
```

- merged

```json
{
  "object1": {
      "param1": true,
      "param2": true
  },
  "object2": {
      "param3": true // the object was replaced even though the global setting is set to merge
  }
}
```


### Things to keep in mind

The library is doing an in memory merge using recursion. So there are two things 
to keep in mind when using it. 

* The objects used should cannot be too large as they are expected to fit in memory 
at the same time
* The objects should not be too nested since the operations are recursive and you'd
be running the risk of a stack overflow


Download
======

The library is available on **JCenter**. Note that it is still in early development
and things might change with subsequent versions.

To use it in your project, add the following to your project

- Gradle:

```groovy
compile 'com.savvasdalkitsis:json-merge:0.0.3'
```

- Maven:

```xml
<dependency>
  <groupId>com.savvasdalkitsis</groupId>
  <artifactId>json-merge</artifactId>
  <version>0.0.3</version>
</dependency>
```

License
-------

    Copyright 2018 Savvas Dalkitsis

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.