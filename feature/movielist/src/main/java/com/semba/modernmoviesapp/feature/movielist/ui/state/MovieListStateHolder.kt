package com.semba.modernmoviesapp.feature.movielist.ui.state

import androidx.compose.runtime.Stable
import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.data.model.network.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow

sealed class ResultState {
    class Loading(val isLoading: Boolean): ResultState()
    class Success(val movieItems: List<MovieItem> = emptyList()): ResultState()
    class Error(val errorCode: ErrorCode?): ResultState()
}

data class MovieListUiState(
    val currentPage: Int = 1,
    val movieItems: List<MovieItem> = emptyList(),
    val limitReached: Boolean = false,
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)

fun MutableStateFlow<MovieListUiState>.reset() {
    this.value = MovieListUiState()
}

@Stable
interface TopBarState {
    val offset: Float
    val height: Float
    val progress: Float
    val consumed: Float
    var scrollTopLimitReached: Boolean
    var scrollOffset: Float
}

data class ScaleAndAlphaArgs(
    val fromScale: Float,
    val toScale: Float,
    val fromAlpha: Float,
    val toAlpha: Float
)

enum class TransitionState { PLACING, PLACED }