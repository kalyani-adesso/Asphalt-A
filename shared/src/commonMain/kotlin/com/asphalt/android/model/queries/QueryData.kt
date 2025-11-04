package com.asphalt.android.model.queries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryResponseDTO(
    @SerialName("query_title")
    val queryTitle: String,
    @SerialName("query_description")
    val queryDescription: String,
    @SerialName("posted_by")
    val postedBy: String,
    @SerialName("posted_on")
    val postedOn: String,
    @SerialName("category_id")
    val categoryId: Int,
    val answers: Map<String, AnswerDTO>? = null,
    val likes: Map<String, Boolean>? = null
)
@Serializable
data class QueryRequestDTO(
    @SerialName("query_title")
    val queryTitle: String,
    @SerialName("query_description")
    val queryDescription: String,
    @SerialName("posted_by")
    val postedBy: String,
    @SerialName("posted_on")
    val postedOn: String,
    @SerialName("category_id")
    val categoryId: Int,
)


data class QueryDomain(
    val id: String,
    val title: String,
    val description: String,
    val postedBy: String,
    val postedOn: String,
    val categoryId: Int,
    val answers: List<AnswerDomain>,
    val likes: List<String>
)



