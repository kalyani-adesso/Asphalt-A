package com.asphalt.queries

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.queries.QueryResponseDTO
import com.asphalt.android.network.queries.QueryAPIService
import com.asphalt.android.repository.queries.QueryRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetAQueryTest {
    @Mock
    lateinit var apiService: QueryAPIService

    private lateinit var repository: QueryRepository

    @Before
    fun setup() {
        repository = QueryRepository(apiService)
    }

    @Test
    fun `getQueryByID returns mapped QueryDomain when api succeeds`() = runTest {
        // Given
        val queryId = "query123"

        val dto = QueryResponseDTO(
            queryTitle = "Test Title",
            queryDescription = "Test Description",
            postedBy = "John",
            postedOn = "2024-01-01",
            categoryId = 1,
            answers = emptyMap(),
            likes = mapOf("user1" to true)
        )

        whenever(apiService.getQuery(queryId))
            .thenReturn(APIResult.Success(dto))

        // When
        val result = repository.getQueryByID(queryId)

        // Then
        assertTrue(result is APIResult.Success)

        val data = (result as APIResult.Success).data

        assertEquals(queryId, data.id)
        assertEquals("Test Title", data.title)
        assertEquals("Test Description", data.description)
        assertEquals("John", data.postedBy)
        assertEquals("2024-01-01", data.postedOn)
        assertEquals(1, data.categoryId)
        assertEquals(1, data.likes.size)
    }

    @Test
    fun `getQueryByID returns error when api fails`() = runTest {

        val exception = Exception("Network Error")

        whenever(apiService.getQuery("1"))
            .thenReturn(APIResult.Error(exception))

        val result = repository.getQueryByID("1")

        assertTrue(result is APIResult.Error)

        assertEquals(
            exception,
            (result as APIResult.Error).exception
        )
    }
}