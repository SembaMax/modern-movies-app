package com.semba.modernmoviesapp.feature.movielist.domain

interface MovieListScreenContract {
    fun loadNextPage()
    fun onSearchClick()
    fun updateQuery(query: String)
}