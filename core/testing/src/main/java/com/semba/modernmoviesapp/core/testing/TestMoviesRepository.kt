package com.semba.modernmoviesapp.core.testing

import com.semba.modernmoviesapp.core.common.DataResponse
import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.data.repository.Repository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TestMoviesRepository : Repository {

    private val moviesFlow: MutableSharedFlow<List<MovieItem>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    private var shouldSuccess = true
    private var error: ErrorCode = ErrorCode.UNSPECIFIED


    fun setMovieItems(movies: List<MovieItem>)
    {
        moviesFlow.tryEmit(movies)
    }

    fun setIsSuccessful(isSuccessful: Boolean, errorCode: ErrorCode? = null)
    {
        shouldSuccess = isSuccessful
        error = errorCode ?: ErrorCode.UNSPECIFIED
    }

    override fun loadLatestMovies(pageIndex: Int): Flow<DataResponse<List<MovieItem>>> {
        return if (shouldSuccess)
            moviesFlow.map { movies -> DataResponse.Success(data = movies) }
        else
            flow { emit(DataResponse.Failure(errorCode = error)) }
    }

    override fun searchMovies(query: String, pageIndex: Int): Flow<DataResponse<List<MovieItem>>> {
        return if (shouldSuccess)
            moviesFlow.map { movies -> DataResponse.Success(data = movies) }
        else
            flow { emit(DataResponse.Failure(errorCode = error)) }
    }

    override fun loadMovieDetail(movieId: Int): Flow<DataResponse<MovieItem>> {
        return if (shouldSuccess)
            moviesFlow.map { movies -> DataResponse.Success(data = movies.first{ it.id == movieId }) }
        else
            flow { emit(DataResponse.Failure(errorCode = error)) }
    }
}