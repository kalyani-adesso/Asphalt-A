package com.asphalt.queries

import com.asphalt.android.model.APIResult
import com.asphalt.android.network.queries.QueryAPIService
import com.asphalt.android.repository.queries.QueryRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class LikeUnLikeQueryTest {
    @Mock
    lateinit var apiService: QueryAPIService

    private lateinit var repository: QueryRepository
    @Before
    fun setUp() {
        repository = QueryRepository(apiService)
    }
    @Test
    fun `likeOrDislikeAnswer should return success when api succeeds`() = runTest {

        // Arrange
        whenever(
            apiService.likeOrDislikeAnswer(
                any(),
                any(),
                any(),
                any()
            )
        ).thenReturn(APIResult.Success(Unit))

        // Act
        val result = repository.likeOrDislikeAnswer(
            userId = "user123",
            queryId = "query123",
            answerId = "answer123",
            isLike = true
        )

        // Assert
        assertTrue(result is APIResult.Success)

        verify(apiService).likeOrDislikeAnswer(
            "query123",
            "answer123",
            "user123",
            true
        )
    }

    @Test
    fun `likeOrDislikeAnswer should return error when api fails`() = runTest {

        // Arrange
        val exception = Exception("Network error")

        whenever(
            apiService.likeOrDislikeAnswer(
                any(),
                any(),
                any(),
                any()
            )
        ).thenReturn(APIResult.Error(exception))

        // Act
        val result = repository.likeOrDislikeAnswer(
            userId = "user123",
            queryId = "query123",
            answerId = "answer123",
            isLike = true
        )

        // Assert
        assertTrue(result is APIResult.Error)

        verify(apiService).likeOrDislikeAnswer(
            "query123",
            "answer123",
            "user123",
            true
        )
    }
    @Test
    fun `likeOrDislikeAnswer should pass false when user dislikes answer`() = runTest {

        // Arrange
        whenever(
            apiService.likeOrDislikeAnswer(
                any(),
                any(),
                any(),
                any()
            )
        ).thenReturn(APIResult.Success(Unit))

        // Act
        val result = repository.likeOrDislikeAnswer(
            userId = "user123",
            queryId = "query123",
            answerId = "answer123",
            isLike = false
        )

        // Assert
        assertTrue(result is APIResult.Success)

        verify(apiService).likeOrDislikeAnswer(
            "query123",
            "answer123",
            "user123",
            false
        )
    }
}