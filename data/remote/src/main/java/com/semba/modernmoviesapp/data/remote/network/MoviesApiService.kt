package com.semba.modernmoviesapp.data.remote.network

import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.data.model.network.PagedMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {

    companion object {
        const val AUTHORIZATION_HEADER_KEY = "Authorization"
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val DISCOVER_MOVIES_ENDPOINT = "discover/movie"
        const val SEARCH_MOVIES_ENDPOINT = "search/movie"
        const val MOVIE_DETAIL_ENDPOINT = "movie/{movieId}"
    }

    @GET(DISCOVER_MOVIES_ENDPOINT)
    suspend fun latest(
        @Query("page") page: Int = 1,
        @Query("release_date.gte") releaseDate: String = "2022",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("language") lang: String = "en-US"
    ): Response<PagedMoviesResponse>

    @GET(SEARCH_MOVIES_ENDPOINT)
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): Response<PagedMoviesResponse>

    @GET(MOVIE_DETAIL_ENDPOINT)
    suspend fun movieDetail(
        @Path("movieId") movieId: Int
    ): Response<MovieItem>
}