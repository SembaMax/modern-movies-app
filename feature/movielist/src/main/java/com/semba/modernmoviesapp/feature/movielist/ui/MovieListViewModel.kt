package com.semba.modernmoviesapp.feature.movielist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.core.common.errorMessage
import com.semba.modernmoviesapp.core.design.navigation.MOVIE_ID_ARG
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.feature.movielist.domain.GetLatestMoviesUseCase
import com.semba.modernmoviesapp.feature.movielist.domain.MovieListScreenContract
import com.semba.modernmoviesapp.feature.movielist.domain.SearchMoviesUseCase
import com.semba.modernmoviesapp.feature.movielist.ui.state.MovieListUiState
import com.semba.modernmoviesapp.feature.movielist.ui.state.ResultState
import com.semba.modernmoviesapp.feature.movielist.ui.state.reset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val getLatestMoviesUseCase: GetLatestMoviesUseCase,
                                             private val searchMoviesUseCase: SearchMoviesUseCase) : ViewModel(),
    MovieListScreenContract {

    private val _uiState = MutableStateFlow(MovieListUiState())

    val uiState =_uiState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        MovieListUiState()
    )

    private val _queryState = MutableStateFlow("")
    val queryState = _queryState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ""
    )

    init {
        loadLatestMovies(_uiState.value.currentPage)
    }

    override fun onSearchClick() {
        _uiState.reset()
        searchMovie(queryState.value, _uiState.value.currentPage)
    }

    override fun loadNextPage() {
        increasePage()
        loadLatestMovies(_uiState.value.currentPage)
    }

    private fun loadLatestMovies(currentPage: Int) {
        getLatestMoviesUseCase(currentPage)
            .onEach { result ->
                when(result)
                {
                    is ResultState.Success -> {
                        checkIfLimitReached(result.movieItems)
                        _uiState.value = _uiState.value.copy(movieItems = _uiState.value.movieItems + result.movieItems)
                    }
                    is ResultState.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(errorMsg = result.errorCode?.errorMessage())
                        checkIfLimitReached(result.errorCode)
                    }
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchMovie(query: String, page: Int) {
        searchMoviesUseCase(query, page)
            .onEach { result ->
                when(result)
                {
                    is ResultState.Success -> {
                        checkIfLimitReached(result.movieItems)
                        _uiState.value = _uiState.value.copy(movieItems = _uiState.value.movieItems + result.movieItems)
                    }
                    is ResultState.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(errorMsg = result.errorCode?.errorMessage())
                        checkIfLimitReached(result.errorCode)
                    }
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }

    override fun updateQuery(query: String) {
        _queryState.value = query
    }

    private fun increasePage() {
        _uiState.value = _uiState.value.copy(currentPage = getNextPage(_uiState.value.currentPage))
    }

    private fun checkIfLimitReached(errorCode: ErrorCode?) {
        val isLimitReached = errorCode == ErrorCode.NOT_FOUND
        _uiState.value = _uiState.value.copy(limitReached = isLimitReached)
    }

    private fun checkIfLimitReached(items: List<MovieItem>) {
        val isLimitReached = items.isEmpty()
        _uiState.value = _uiState.value.copy(limitReached = isLimitReached)
    }

    private fun getNextPage(page: Int): Int = page + 1
}

fun MovieItem.toArgs(): Map<String,String> = mapOf(
    MOVIE_ID_ARG to this.id.toString()
)