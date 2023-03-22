package com.semba.modernmoviesapp.feature.moviedetail.domain

import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.data.repository.Repository
import com.semba.modernmoviesapp.feature.moviedetail.ui.DetailUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(movieId: Int): Flow<DetailUiState> =
        repository.loadMovieDetail(movieId)
            .map {
                if (it.errorCode != null)
                    DetailUiState.Error(it.errorCode)
                else {
                    DetailUiState.Success(it.data ?: MovieItem.empty())
                }
            }
            .catch { emit(DetailUiState.Error(ErrorCode.UNSPECIFIED)) }
            .onStart { emit(DetailUiState.Loading) }
}