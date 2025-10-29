package com.asphalt.android.repository.queries

import com.asphalt.android.mapApiResult
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.queries.AnswerDTO
import com.asphalt.android.model.queries.AnswerDomain
import com.asphalt.android.model.queries.AnswerRequestDTO
import com.asphalt.android.model.queries.QueryDomain
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.model.queries.QueryResponseDTO
import com.asphalt.android.network.APIService

class QueryRepository(private val apiService: APIService) {
    suspend fun getQueries(): APIResult<List<QueryDomain>> {
        return apiService.getQueries().mapApiResult { response ->
            response?.toDomain().orEmpty()
        }
    }


    private fun Map<String, QueryResponseDTO>?.toDomain(): List<QueryDomain>? {
        return this?.map { (id, rawQuery) ->
            QueryDomain(
                id = id,
                title = rawQuery.queryTitle,
                description = rawQuery.queryDescription,
                postedBy = rawQuery.postedBy,
                postedOn = rawQuery.postedOn,
                categoryId = rawQuery.categoryId,
                answers = rawQuery.answers.toAnswersDomain(),
                likes = rawQuery.likes.orEmpty().map { (id, _) -> id },
            )
        }
    }

    private fun Map<String, AnswerDTO>?.toAnswersDomain(): List<AnswerDomain> {

        return this.orEmpty().map { (id, rawAnswer) ->
            AnswerDomain(
                id,
                rawAnswer.answer,
                rawAnswer.answeredBy,
                rawAnswer.answeredOn,
                rawAnswer.likes ?: emptyList(),
                rawAnswer.dislikes ?: emptyList()
            )
        }

    }

    suspend fun addQuery(
        queryTitle: String,
        queryDescription: String,
        categoryId: Int,
        postedOn: String,
        postedBy: String
    ): APIResult<Unit> {
        val request = QueryRequestDTO(
            queryTitle = queryTitle,
            queryDescription = queryDescription,
            categoryId = categoryId,
            postedOn = postedOn,
            postedBy = postedBy,
        )
        return apiService.postQuery(request).mapApiResult {
        }
    }

    suspend fun addAnswer(
        queryId: String,
        answer: String,
        answeredBy: String,
        answeredOn: String
    ): APIResult<Unit> {
        val request = AnswerRequestDTO(answer, answeredBy, answeredOn)
        return apiService.postAnswer(queryId, request).mapApiResult {
        }
    }

}


