package com.semba.modernmoviesapp.data.repository

import com.semba.modernmoviesapp.core.common.DataResponse
import com.semba.modernmoviesapp.data.model.network.MovieItem
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun loadLatestMovies(pageIndex: Int): Flow<DataResponse<List<MovieItem>>>
    fun searchMovies(query: String, pageIndex: Int): Flow<DataResponse<List<MovieItem>>>
    fun loadMovieDetail(movieId: Int): Flow<DataResponse<MovieItem>>

}