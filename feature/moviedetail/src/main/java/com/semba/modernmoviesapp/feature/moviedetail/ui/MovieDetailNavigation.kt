package com.semba.modernmoviesapp.feature.moviedetail.ui

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.semba.modernmoviesapp.core.design.navigation.MOVIE_ID_ARG
import com.semba.modernmoviesapp.core.design.navigation.withArgs

const val movieDetailRoute = "movie_detail_screen_route/{${MOVIE_ID_ARG}}"

fun NavController.navigateToMovieDetailScreen(args: Map<String, String>, navOptions: NavOptions? = null) {
    this.navigate(movieDetailRoute.withArgs(args), navOptions)
}

private val args = listOf(
    navArgument(MOVIE_ID_ARG) {
        type = NavType.IntType
    },
)

fun NavGraphBuilder.movieDetailScreen() {
    composable(route = movieDetailRoute, arguments = args) { navBackStackEntry ->
        val movieId = navBackStackEntry.arguments?.getInt(MOVIE_ID_ARG) ?: 0
        MovieDetailRoute(movieId = movieId)
    }
}