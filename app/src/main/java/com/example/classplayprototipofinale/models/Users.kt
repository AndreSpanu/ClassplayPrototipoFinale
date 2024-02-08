package com.example.classplayprototipofinale.models

data class Users(var username: String? = null) {
    var emailAddress: String? = null
    var phoneNumber: String? = null
    var profileImgUrl: String? = null
    var lastResearch: MutableList<String>? = null
    var yourToDo: MutableMap<String, ToDo>? = null
    var bio: String? = null
}
