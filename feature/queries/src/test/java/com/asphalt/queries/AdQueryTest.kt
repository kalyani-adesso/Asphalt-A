package com.asphalt.queries

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.network.queries.QueryAPIService
import com.asphalt.android.repository.queries.QueryRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AdQueryTest {
    @Mock
    private lateinit var apiService: QueryAPIService

    private lateinit var repository: QueryRepository

    @Before
    fun setUp() {
        repository = QueryRepository(apiService)
    }

    @Test
    fun `addQuery should return success`() = runTest {
        // Arrange
        val response = GenericResponse("query123")

        whenever(apiService.postQuery(any()))
            .thenReturn(APIResult.Success(response))

        // Act
        val result = repository.addQuery(
            queryTitle = "Road Issue",
            queryDescription = "Large pothole",
            categoryId = 1,
            postedOn = "2025-01-01",
            postedBy = "user123"
        )

        // Assert
        assertTrue(result is APIResult.Success)
        assertEquals(response, (result as APIResult.Success).data)

        val captor = argumentCaptor<QueryRequestDTO>()
        verify(apiService).postQuery(captor.capture())

        assertEquals("Road Issue", captor.firstValue.queryTitle)
        assertEquals("Large pothole", captor.firstValue.queryDescription)
        assertEquals(1, captor.firstValue.categoryId)
        assertEquals("2025-01-01", captor.firstValue.postedOn)
        assertEquals("user123", captor.firstValue.postedBy)
    }

    @Test
    fun `addQuery returns error`() = runTest {
        // Arrange
        val exception = RuntimeException("Server Error")

        whenever(apiService.postQuery(any()))
            .thenReturn(APIResult.Error(exception, 500))

        // Act
        val result = repository.addQuery(
            queryTitle = "Road Issue",
            queryDescription = "Large pothole",
            categoryId = 1,
            postedOn = "2025-01-01",
            postedBy = "user123"
        )

        // Assert
        assertTrue(result is APIResult.Error)

        result as APIResult.Error
        assertEquals(exception, result.exception)
        assertEquals("Server Error", result.exception.message)
        assertEquals(500, result.code)

        verify(apiService).postQuery(any())
    }


}