package com.semba.modernmoviesapp.core.design.navigation

import timber.log.Timber

const val MOVIE_ID_ARG = "movieId"

fun String.withArgs(args: Map<String, String>): String {
    var result = this
    args.forEach { (key, value) ->
        result = result.replace("{$key}", value)
    }
    Timber.d("Detail screen route with args: $result")
    return result
}