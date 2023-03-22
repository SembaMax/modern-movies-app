package com.semba.modernmoviesapp.feature.movielist.domain

import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.data.repository.Repository
import com.semba.modernmoviesapp.feature.movielist.ui.state.ResultState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetLatestMoviesUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(page: Int): Flow<ResultState> =
        repository.loadLatestMovies(page)
            .map {
                if (it.errorCode != null)
                    ResultState.Error(it.errorCode)
                else {
                    ResultState.Success(it.data ?: emptyList())
                }
            }
            .catch { emit(ResultState.Error(ErrorCode.UNSPECIFIED)) }
            .onStart { emit(ResultState.Loading(true)) }
            .onCompletion { emit(ResultState.Loading(false)) }

}