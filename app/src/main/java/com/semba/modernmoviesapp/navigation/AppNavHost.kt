package com.semba.modernmoviesapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.semba.modernmoviesapp.core.design.navigation.ScreenDestination
import com.semba.modernmoviesapp.feature.moviedetail.ui.movieDetailScreen
import com.semba.modernmoviesapp.feature.movielist.ui.movieListRoute
import com.semba.modernmoviesapp.feature.movielist.ui.movieListScreen

@Composable
fun AppNavHost (
    navController: NavHostController,
    onBackClick: () -> Unit,
    navigateTo: (screenDestination: ScreenDestination, args: Map<String, String>) -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = movieListRoute
)
{
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        movieListScreen(navigateTo)
        movieDetailScreen()
    }
}