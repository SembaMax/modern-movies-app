package com.semba.modernmoviesapp.feature.movielist

import app.cash.turbine.test
import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.core.common.errorMessage
import com.semba.modernmoviesapp.core.testing.MainDispatcherRule
import com.semba.modernmoviesapp.core.testing.TestMoviesRepository
import com.semba.modernmoviesapp.feature.movielist.domain.GetLatestMoviesUseCase
import com.semba.modernmoviesapp.feature.movielist.domain.SearchMoviesUseCase
import com.semba.modernmoviesapp.feature.movielist.ui.MovieListViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MovieListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: TestMoviesRepository
    private lateinit var getLatestMoviesUseCase: GetLatestMoviesUseCase
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase
    private lateinit var viewModel: MovieListViewModel

    @Before
    fun setup() {
        repository = TestMoviesRepository()
        getLatestMoviesUseCase = GetLatestMoviesUseCase(repository)
        searchMoviesUseCase = SearchMoviesUseCase(repository)
        viewModel = MovieListViewModel(getLatestMoviesUseCase, searchMoviesUseCase)
    }

    @Test
    fun `fetching the correct page index`() = runTest {

        repository.setIsSuccessful(true)
        repository.setMovieItems(emptyList())
        viewModel.loadNextPage()
        viewModel.uiState.test {
            assertEquals(awaitItem().currentPage, 2)
        }
    }

    @Test
    fun `state is error on failure request`() = runTest {

        repository.setIsSuccessful(false)
        viewModel.loadNextPage()

        viewModel.uiState.test {
            assertEquals(awaitItem().errorMsg, ErrorCode.UNSPECIFIED.errorMessage())
        }
    }

    @Test
    fun `limit is reached on 404 errors`() = runTest {

        repository.setIsSuccessful(false, ErrorCode.NOT_FOUND)
        viewModel.loadNextPage()

        viewModel.uiState.test {
            val item = awaitItem()
            assertEquals(item.errorMsg, ErrorCode.NOT_FOUND.errorMessage())
            assertEquals(item.limitReached, true)
        }
    }

    @Test
    fun `limit is reached on empty response`() = runTest {

        repository.setIsSuccessful(true)
        repository.setMovieItems(emptyList())
        viewModel.loadNextPage()

        viewModel.uiState.test {
            val item = awaitItem()
            assertEquals(item.errorMsg, null)
            assertEquals(item.limitReached, true)
        }
    }
}