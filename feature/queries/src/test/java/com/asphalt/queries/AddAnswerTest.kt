package com.asphalt.queries

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.queries.AnswerRequestDTO
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.network.queries.QueryAPIService
import com.asphalt.android.repository.queries.QueryRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddAnswerTest {
    @Mock
    private lateinit var apiService: QueryAPIService
    private lateinit var repository: QueryRepository

    @Before
    fun setUp() {
        repository = QueryRepository(apiService)
    }
    @Test
    fun `addAnswer should return success`() = runTest {
        // Arrange
        val response = GenericResponse("answer123")

        whenever(apiService.postAnswer(any(), any()))
            .thenReturn(APIResult.Success(response))

        // Act
        val result = repository.addAnswer(
            queryId = "query123",
            answer = "This is an answer",
            answeredBy = "user123",
            answeredOn = "2025-01-01"
        )

        // Assert
        assertTrue(result is APIResult.Success)
        assertEquals(response, (result as APIResult.Success).data)

        val queryIdCaptor = argumentCaptor<String>()
        val requestCaptor = argumentCaptor<AnswerRequestDTO>()

        verify(apiService).postAnswer(
            queryIdCaptor.capture(),
            requestCaptor.capture()
        )

        assertEquals("query123", queryIdCaptor.firstValue)
        assertEquals("This is an answer", requestCaptor.firstValue.answer)
        assertEquals("user123", requestCaptor.firstValue.answeredBy)
        assertEquals("2025-01-01", requestCaptor.firstValue.answeredOn)
    }

    @Test
    fun addAnswer_returnsError() = runTest {
        // Arrange
        val request = AnswerRequestDTO(
            answer = "Answer",
            answeredBy = "user1",
            answeredOn = "2025-01-01"
        )

        val error = APIResult.Error(
            code = 500,
            exception = Exception("Internal Server Error")
        )

        Mockito.`when`(
            apiService.postAnswer("query123", request)
        ).thenReturn(error)

        // Act
        val result = repository.addAnswer(
            "query123",
            "Answer",
            "user1",
            "2025-01-01"
        )

        // Assert
        assertEquals(error, result)
    }


}