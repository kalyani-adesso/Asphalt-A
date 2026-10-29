package com.asphalt.queries

import com.asphalt.android.model.APIResult
import com.asphalt.android.network.queries.QueryAPIService
import com.asphalt.android.repository.queries.QueryRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class LikeUnLikeQueryApiTest {
    @Mock
    private lateinit var apiService: QueryAPIService

    private lateinit var repository: QueryRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = QueryRepository(apiService)
    }
    @Test
    fun `likeQuery should return success`() = runTest {

        // Arrange
        whenever(
            apiService.likeQuery(any(), any())
        ).thenReturn(APIResult.Success(Unit))

        // Act
        val result = repository.likeQuery(
            userId = "user123",
            queryId = "query123"
        )

        // Assert
        assertTrue(result is APIResult.Success)

        val queryIdCaptor = argumentCaptor<String>()
        val userIdCaptor = argumentCaptor<String>()

        verify(apiService).likeQuery(
            queryIdCaptor.capture(),
            userIdCaptor.capture()
        )

        assertEquals("query123", queryIdCaptor.firstValue)
        assertEquals("user123", userIdCaptor.firstValue)
    }
    @Test
    fun `removeLikeQuery should return error when api fails`() = runTest {

        // Arrange
        val exception = Exception("Network error")

        whenever(
            apiService.deleteLikeQuery(any(), any())
        ).thenReturn(APIResult.Error(exception))

        // Act
        val result = repository.removeLikeQuery(
            userId = "user123",
            queryId = "query123"
        )

        // Assert
        assertTrue(result is APIResult.Error)

        verify(apiService).deleteLikeQuery(
            "query123",
            "user123"
        )
    }

}