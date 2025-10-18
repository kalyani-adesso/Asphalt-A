package com.asphalt.queries.data

data class Answer(
    val answeredByName: String,
    val answeredByUrl: String,
    val answeredOn: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val answer: String,
    val isMechanic: Boolean = false
)
