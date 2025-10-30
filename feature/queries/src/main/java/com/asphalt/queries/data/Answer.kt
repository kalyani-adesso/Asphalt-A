package com.asphalt.queries.data

data class Answer(
    val id: String,
    val answeredByName: String,
    val answeredByUrl: String,
    val answeredOn: Long,
    val likeCount: Int,
    val dislikeCount: Int,
    val answer: String,
    val isMechanic: Boolean = false,
    val isUserLiked: Boolean = false,
    val isUserDisliked: Boolean = false,
)
