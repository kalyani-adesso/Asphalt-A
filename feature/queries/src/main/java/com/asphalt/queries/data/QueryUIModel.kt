package com.asphalt.queries.data

data class Query(
    val id: String,
    val title: String,
    val description: String,
    val categoryId: Int,
    val isAnswered: Boolean,
    val postedOn: Long,
    val postedByName: String,
    val postedByUrl: String,
    val likeCount: Int,
    val answerCount: Int,
    val answers: List<Answer>,
    val isUserLiked: Boolean = false
)


