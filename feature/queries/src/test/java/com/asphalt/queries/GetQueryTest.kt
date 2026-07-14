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
class GetQueryTest {
    @Mock
    private lateinit var apiService: QueryAPIService

    private lateinit var repository: QueryRepository

    @Before
    fun setup() {
        repository = QueryRepository(apiService)
    }

    @Test
    fun `getQueries returns mapped list when api succeeds`() = runTest {
        // Given
        val response = mapOf(
            "1" to QueryResponseDTO(
                queryTitle = "Test Title",
                queryDescription = "Test Description",
                postedBy = "User1",
                postedOn = "2024-01-01",
                categoryId = 1,
                answers = null,
                likes = mapOf(
                    "user1" to true,
                    "user2" to true
                )
            )
        )

        whenever(apiService.getQueries())
            .thenReturn(APIResult.Success(response))

        // When
        val result = repository.getQueries()

        // Then
        assertTrue(result is APIResult.Success)

        val data = (result as APIResult.Success).data

        assertEquals(1, data.size)

        val query = data.first()

        assertEquals("1", query.id)
        assertEquals("Test Title", query.title)
        assertEquals("Test Description", query.description)
        assertEquals("User1", query.postedBy)
        assertEquals("2024-01-01", query.postedOn)
        assertEquals(1, query.categoryId)
        assertEquals(2, query.likes.size)
    }

    @Test
    fun `getQueries returns empty list when response body is null`() = runTest {

        whenever(apiService.getQueries())
            .thenReturn(APIResult.Success(null))

        val result = repository.getQueries()

        assertTrue(result is APIResult.Success)

        val data = (result as APIResult.Success).data

        assertTrue(data.isEmpty())
    }

    @Test
    fun `getQueries returns failure when api fails`() = runTest {

        val exception = Exception("Network Error")

        whenever(apiService.getQueries())
            .thenReturn(APIResult.Error(exception))

        val result = repository.getQueries()

        assertTrue(result is APIResult.Error)

        assertEquals(
            "Network Error",
            (result as APIResult.Error).exception.message
        )
    }

}