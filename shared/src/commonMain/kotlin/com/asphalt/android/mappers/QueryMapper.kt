package com.asphalt.android.mappers

import com.asphalt.android.model.queries.AnswerDTO
import com.asphalt.android.model.queries.AnswerDomain
import com.asphalt.android.model.queries.QueryDomain
import com.asphalt.android.model.queries.QueryResponseDTO

fun Map<String, QueryResponseDTO>?.toDomain(): List<QueryDomain>? {
    return this?.map { (id, rawQuery) ->
        rawQuery.toQueryDomain(id)
    }
}

fun QueryResponseDTO.toQueryDomain(id: String): QueryDomain {
    return with(this) {
        QueryDomain(
            id,
            queryTitle,
            queryDescription,
            postedBy,
            postedOn,
            categoryId,
            answers.toAnswersDomain(),
            likes = likes.orEmpty().map { (id, _) -> id },
        )
    }
}

fun Map<String, AnswerDTO>?.toAnswersDomain(): List<AnswerDomain> {

    return this.orEmpty().map { (id, rawAnswer) ->
        rawAnswer.toAnswerDomain(id)
    }
}

fun AnswerDTO.toAnswerDomain(id: String): AnswerDomain {
    return with(this) {
        val likesDislikesMap = likesDislikes.orEmpty()
        AnswerDomain(
            id,
            answer,
            answeredBy,
            answeredOn,
            likes = likesDislikesMap
                .filterValues { it }
                .keys.toList(),

            dislikes = likesDislikesMap
                .filterValues { !it }
                .keys.toList(),
        )
    }
}