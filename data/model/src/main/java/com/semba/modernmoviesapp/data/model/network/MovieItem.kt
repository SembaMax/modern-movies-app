package com.semba.modernmoviesapp.data.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieItem (
        val adult: Boolean? = null,
        val genres: List<MovieGenre>? = null,
        val id: Int? = null,
        @SerialName("imdb_id") val imdbId: String? = null,
        @SerialName("original_language") val originalLanguage: String? = null,
        @SerialName("original_title") val originalTitle: String? = null,
        val overview: String? = null,
        val popularity: Float? = null,
        @SerialName("poster_path") val posterPath: String? = null,
        @SerialName("production_companies") val productionCompanies: List<ProductionCompany>? = null,
        @SerialName("production_countries") val productionCountries: List<ProductionCountry>? = null,
        @SerialName("release_date") val releaseDate: String? = null,
        val revenue: Double? = null,
        val runtime: Double? = null,
        @SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguage>? = null,
        val status: String? = null,
        val tagline: String? = null,
        val title: String? = null,
        val video: Boolean? = null,
        @SerialName("vote_average") val voteAverage: Float? = null,
        @SerialName("vote_count") val voteCount: Int? = null,
)
{
        companion object {
                fun empty() = MovieItem()
        }
}

@Serializable
data class MovieGenre(
        val id: Int? = null,
        val name: String? = null
)

@Serializable
data class ProductionCompany(
        val id: Int? = null,
        @SerialName("logo_path") val logoPath: String? = null,
        val name: String? = null,
        @SerialName("origin_country") val originCountry: String? = null
)

@Serializable
data class ProductionCountry(
        @SerialName("iso_3166_1") val iso: String? = null,
        val name: String? = null
)

@Serializable
data class SpokenLanguage(
        @SerialName("iso_639_1") val iso: String? = null,
        val name: String? = null
)