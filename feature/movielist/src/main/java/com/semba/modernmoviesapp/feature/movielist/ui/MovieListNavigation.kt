package com.semba.modernmoviesapp.feature.movielist.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.semba.modernmoviesapp.core.design.navigation.ScreenDestination

const val movieListRoute = "movie_list_screen_route"

fun NavController.navigateToMovieListScreen(navOptions: NavOptions? = null) {
    this.navigate(movieListRoute, navOptions)
}

fun NavGraphBuilder.movieListScreen(navigateTo: (screenDestination: ScreenDestination, args: Map<String, String>) -> Unit) {
    composable(route = movieListRoute) {
        MovieListRoute(navigateTo = navigateTo)
    }
}