package com.asphalt.queries.repositories

import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.data.Query
import com.asphalt.queries.data.bikeQueries
import kotlinx.coroutines.delay

class QueryRepo {
    suspend fun loadQueryList(): List<Query> {
        delay(200)
        return bikeQueries.sortedByDescending { query ->
            Utils.parseISODateToMillis(query.postedOn)
        }

    }

    suspend fun addQuery(query: Query) {
        delay(200)
        bikeQueries = bikeQueries.plus(query)
    }
}