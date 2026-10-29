package com.asphalt.queries

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.queries.AnswerDTO
import com.asphalt.android.network.queries.QueryAPIService
import com.asphalt.android.repository.queries.QueryRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetAnswerByIdTest {
    @Mock
    private lateinit var apiService: QueryAPIService

    private lateinit var repository: QueryRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = QueryRepository(apiService)
    }

    @Test
    fun `getAnswerByID returns Success when API succeeds`() = runTest {
        // Arrange
        val queryId = "query1"
        val answerId = "answer1"

        val dto = AnswerDTO(
            answer = "This is an answer",
            answeredBy = "John",
            answeredOn = "2024-01-01",
            likesDislikes = mapOf(
                "user1" to true,
                "user2" to false
            )
        )

        whenever(apiService.getAnswer(queryId, answerId))
            .thenReturn(APIResult.Success(dto))

        // Act
        val result = repository.getAnswerByID(queryId, answerId)

        // Assert
        assertTrue(result is APIResult.Success)

        val data = (result as APIResult.Success).data
        assertEquals(answerId, data.id)
        assertEquals("This is an answer", data.answer)
        assertEquals("John", data.answeredBy)
        assertEquals("2024-01-01", data.answeredOn)
        assertEquals(listOf("user1"), data.likes)
        assertEquals(listOf("user2"), data.dislikes)
    }
    @Test
    fun `getAnswerByID returns Error when API fails`() = runTest {
        // Arrange
        val exception = RuntimeException("Network Error")

        whenever(apiService.getAnswer("query1", "answer1"))
            .thenReturn(APIResult.Error(exception, 500))

        // Act
        val result = repository.getAnswerByID("query1", "answer1")

        // Assert
        assertTrue(result is APIResult.Error)

        val error = result as APIResult.Error
        assertEquals(exception, error.exception)
        assertEquals(500, error.code)
    }

    @Test
    fun `getAnswerByID returns null when API returns null`() = runTest {
        // Arrange
        whenever(apiService.getAnswer("query1", "answer1"))
            .thenReturn(null)

        // Act
        val result = repository.getAnswerByID("query1", "answer1")

        // Assert
        assertNull(result)
    }
}