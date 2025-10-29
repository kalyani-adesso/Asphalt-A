package com.asphalt.android.network

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.queries.AnswerRequestDTO
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.model.queries.QueryResponseDTO

interface APIService {
    suspend fun getQueries(): APIResult<Map<String, QueryResponseDTO>?>
    suspend fun postQuery(queryRequestDTO: QueryRequestDTO): APIResult<GenericResponse>
    suspend fun postAnswer(queryId: String, answerRequestDTO: AnswerRequestDTO): APIResult<GenericResponse>
}