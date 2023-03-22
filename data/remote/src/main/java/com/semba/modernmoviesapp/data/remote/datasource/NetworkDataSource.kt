package com.semba.modernmoviesapp.data.remote.datasource

import com.semba.modernmoviesapp.core.common.DataResponse
import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.data.remote.network.MoviesApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val service: MoviesApiService):
    RemoteDataSource {

    override suspend fun fetchLatestMovies(pageIndex: Int): DataResponse<List<MovieItem>> = withContext(
        Dispatchers.IO) {
        try {
            val result = service.latest(page = pageIndex)
            if (result.isSuccessful) {
                Timber.d("Http request is successful. Latest movies with size = ${result.body()?.results?.size}")
                val items = result.body()?.results ?: emptyList()
                items.forEach {
                    it.appendImageUrl()
                }
                DataResponse.Success(items)
            } else {
                val errorCode = result.code()
                Timber.d("Http request is failed. ErrorCode is = $errorCode")
                DataResponse.Failure(ErrorCode.from(errorCode))
            }
        }
        catch (e: Exception)
        {
            DataResponse.Failure(ErrorCode.SERVER_ERROR)
        }
    }

    override suspend fun searchMovies(
        query: String,
        pageIndex: Int
    ): DataResponse<List<MovieItem>> = withContext(
        Dispatchers.IO) {
        try {
            val result = service.search(query = query, page = pageIndex)
            if (result.isSuccessful) {
                Timber.d("Http request is successful. Search movies with size = ${result.body()?.results?.size}")
                val items = result.body()?.results ?: emptyList()
                items.forEach {
                    it.appendImageUrl()
                }
                DataResponse.Success(items)
            } else {
                val errorCode = result.code()
                Timber.d("Http request is failed. ErrorCode is = $errorCode")
                DataResponse.Failure(ErrorCode.from(errorCode))
            }
        }
        catch (e: Exception)
        {
            DataResponse.Failure(ErrorCode.SERVER_ERROR)
        }
    }

    override suspend fun fetchMovieDetail(
        movieId: Int
    ): DataResponse<MovieItem> = withContext(Dispatchers.IO) {
        try {
            val result = service.movieDetail(movieId = movieId)
            if (result.isSuccessful && result.body() != null) {
                Timber.d("Http request is successful. Movie fetched with id ${movieId}}")
                val item = result.body() ?: MovieItem.empty()
                item.appendImageUrl()
                DataResponse.Success(item)
            } else {
                val errorCode = result.code()
                Timber.d("Http request is failed. ErrorCode is = $errorCode")
                DataResponse.Failure(ErrorCode.from(errorCode))
            }
        } catch (e: Exception) {
            Timber.d("Error while fetching movie detail = ${e.message}")
            DataResponse.Failure(ErrorCode.SERVER_ERROR)
        }
    }

    fun MovieItem.appendImageUrl() {
        this.posterPath = "${MoviesApiService.IMAGE_BASE_URL}${this.posterPath}"
    }

}
