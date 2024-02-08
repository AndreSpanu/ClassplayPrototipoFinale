package com.example.classplayprototipofinale.models

data class ToDo(val todoTitle: String? = null) {
    var img: Map<String, String>? = null
    var steps: MutableMap<String, ToDoStep>?  = null
    var description: String? = null
}

data class ToDoStep(var step: Int? = null) {
    var icon: String? = null
    var title: String? = null
    var link: String? = null
    var linkType: String? = null
    var description: String? = null
    var completed: Boolean? = null
    var realAppLink: String? = null
}
