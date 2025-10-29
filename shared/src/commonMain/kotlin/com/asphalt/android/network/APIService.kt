package com.asphalt.android.network

import com.asphalt.android.model.queries.AnswerRequestDTO
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.model.queries.QueryResponseDTO

interface APIService {
    suspend fun getQueries():Map<String, QueryResponseDTO>?
    suspend fun postQuery(queryRequestDTO: QueryRequestDTO)
    suspend fun postAnswer(queryId:String,answerRequestDTO: AnswerRequestDTO)
}