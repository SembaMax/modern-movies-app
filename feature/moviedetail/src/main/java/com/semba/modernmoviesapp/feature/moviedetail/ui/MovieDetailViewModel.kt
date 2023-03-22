package com.semba.modernmoviesapp.feature.moviedetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.feature.moviedetail.domain.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val getMovieDetailUseCase: GetMovieDetailUseCase): ViewModel() {

    private val _uiState: MutableStateFlow<DetailUiState> = MutableStateFlow(DetailUiState.Loading)

    val uiState =_uiState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        DetailUiState.Loading
    )

    fun getMovieDetail(movieId: Int) {
        getMovieDetailUseCase(movieId)
            .onEach {
                _uiState.value = it
            }.launchIn(viewModelScope)
    }
}

fun String.splitTagsString(): List<String> = this.split(", ")

sealed interface DetailUiState {
    object Loading: DetailUiState
    data class Success(val movieItem: MovieItem): DetailUiState
    data class Error(val errorCode: ErrorCode?): DetailUiState
}