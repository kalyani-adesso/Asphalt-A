package com.asphalt.android.model.queries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnswerDTO(
    val answer: String,
    @SerialName("answered_by")
    val answeredBy: String,
    @SerialName("answered_on")
    val answeredOn: String,
    val likes: List<String>? = emptyList(),
    val dislikes: List<String>? = emptyList()
)

data class AnswerDomain(
    val id: String,
    val answer: String,
    val answeredBy: String,
    val answeredOn: String,
    val likes: List<String>,
    val dislikes: List<String>
)
@Serializable
data class AnswerRequestDTO(
    val answer: String,
    @SerialName("answered_by")
    val answeredBy: String,
    @SerialName("answered_on")
    val answeredOn: String,
)