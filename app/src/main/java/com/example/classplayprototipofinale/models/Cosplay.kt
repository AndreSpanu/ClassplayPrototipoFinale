package com.example.classplayprototipofinale.models



data class TutorialStep(var step: Int? = null) {
    var icon: Int? = null
    var componentName: String? = null
    var link: String? = null
    var description: String? = null
    var linkType: String? = null
    var realAppLink: String? = null
}

data class Review(val username: String? = null) {
    var vote: Int? = null
}

data class Cosplay(val cosplayName: String? = null) {
    var username: String? = null
    var date: String? = null
    var imgUrls: MutableMap<String, String>? = null
    var tutorial: MutableMap<String, TutorialStep>? = null
    var description: String? = null
    var avgReviews: String? = null
    var reviews: MutableMap<String, Review>? = null
    var tags: MutableList<String>? = null
    var time: MutableMap<String, Int>? = null
    var favorites: MutableList<String>? = null
    var material: String? = null
}
