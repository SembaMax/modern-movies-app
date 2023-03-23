package com.semba.modernmoviesapp.core.testing

import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.data.model.network.PagedMoviesResponse
import kotlinx.serialization.json.Json
import java.io.InputStreamReader

object TestingUtils {
    fun moviesJsonAsString(): String {
        val resourceAsStream = javaClass.classLoader?.getResourceAsStream("MoviesResponse.json")
        val reader = InputStreamReader(resourceAsStream)
        return reader.use { it.readText() }
    }

    fun moviesJsonAsItems(): List<MovieItem> {
        val jsonString = moviesJsonAsString()
        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString(PagedMoviesResponse.serializer(), jsonString).results ?: listOf()
    }
}