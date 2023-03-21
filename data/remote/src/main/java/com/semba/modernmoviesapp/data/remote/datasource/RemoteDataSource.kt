package com.semba.modernmoviesapp.data.remote.datasource

import com.semba.modernmoviesapp.core.common.DataResponse
import com.semba.modernmoviesapp.data.model.network.MovieItem

interface RemoteDataSource {
    suspend fun fetchLatestMovies(pageIndex: Int): DataResponse<List<MovieItem>>
    suspend fun searchMovies(query: String, pageIndex: Int): DataResponse<List<MovieItem>>
    suspend fun fetchMovieDetail(movieId: Int): DataResponse<MovieItem>
}