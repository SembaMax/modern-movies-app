package com.semba.modernmoviesapp.data.repository

import com.semba.modernmoviesapp.core.common.DataResponse
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.data.remote.datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) : Repository {

    override fun loadLatestMovies(pageIndex: Int): Flow<DataResponse<List<MovieItem>>> = flow {
        val result = remoteDataSource.fetchLatestMovies(pageIndex)
        emit(result)
    }

    override fun loadMovieDetail(movieId: Int): Flow<DataResponse<MovieItem>> = flow {
        val result = remoteDataSource.fetchMovieDetail(movieId)
        emit(result)
    }

    override fun searchMovies(query: String, pageIndex: Int): Flow<DataResponse<List<MovieItem>>> = flow {
        val result = remoteDataSource.searchMovies(query, pageIndex)
        emit(result)
    }

}