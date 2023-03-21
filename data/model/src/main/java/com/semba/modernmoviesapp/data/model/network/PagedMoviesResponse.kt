package com.semba.modernmoviesapp.data.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedMoviesResponse (
        val page: Int? = null,
        val results: List<MovieItem>? = null,
        @SerialName("total_results") val totalResults: Int? = null,
        @SerialName("total_pages") val totalPages: Int? = null
        )