package com.semba.modernmoviesapp.feature.moviedetail

import app.cash.turbine.test
import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.core.testing.MainDispatcherRule
import com.semba.modernmoviesapp.core.testing.TestMoviesRepository
import com.semba.modernmoviesapp.core.testing.TestingUtils
import com.semba.modernmoviesapp.feature.moviedetail.domain.GetMovieDetailUseCase
import com.semba.modernmoviesapp.feature.moviedetail.ui.DetailUiState
import com.semba.modernmoviesapp.feature.moviedetail.ui.MovieDetailViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MovieDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: TestMoviesRepository
    private lateinit var getMovieDetailUseCase: GetMovieDetailUseCase
    private lateinit var viewModel: MovieDetailViewModel

    private val firstMovieId = 804150

    @Before
    fun setup() {
        repository = TestMoviesRepository()
        getMovieDetailUseCase = GetMovieDetailUseCase(repository)
        viewModel = MovieDetailViewModel(getMovieDetailUseCase)
    }

    @Test
    fun `state is initially loading`() = runTest {
        assertEquals(DetailUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `state is success on successful request`() = runTest {

        val result = TestingUtils.moviesJsonAsItems()
        repository.setIsSuccessful(true)
        repository.setMovieItems(result)
        viewModel.getMovieDetail(firstMovieId)

        viewModel.uiState.test {
            assert(awaitItem() is DetailUiState.Success)
        }
    }

    @Test
    fun `state is error on failure request`() = runTest {

        repository.setIsSuccessful(false)
        repository.setMovieItems(emptyList())
        viewModel.getMovieDetail(firstMovieId)

        viewModel.uiState.test {
            assert(awaitItem() is DetailUiState.Error)
        }
    }

    @Test
    fun `emit correct data on successful request`() = runTest {

        val result = TestingUtils.moviesJsonAsItems()
        repository.setIsSuccessful(true)
        repository.setMovieItems(result)
        viewModel.getMovieDetail(firstMovieId)
        val expectedModel = result.firstOrNull{ it.id == firstMovieId }

        viewModel.uiState.test {
            val item = awaitItem()
            assert(item is DetailUiState.Success)
            assertEquals(
                expectedModel,
                (item as DetailUiState.Success).movieItem
            )
        }
    }

    @Test
    fun `emit correct data on failure request`() = runTest {

        repository.setIsSuccessful(false, ErrorCode.DATABASE_ERROR)
        repository.setMovieItems(emptyList())
        viewModel.getMovieDetail(firstMovieId)

        viewModel.uiState.test {
            val item = awaitItem()
            assert(item is DetailUiState . Error)
            assertEquals(
                ErrorCode.DATABASE_ERROR,
                (item as DetailUiState.Error).errorCode
            )
        }
    }

}