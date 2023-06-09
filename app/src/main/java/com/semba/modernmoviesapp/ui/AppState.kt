package com.semba.modernmoviesapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.semba.modernmoviesapp.core.common.connectivity.NetworkMonitor
import com.semba.modernmoviesapp.core.design.navigation.ScreenDestination
import com.semba.modernmoviesapp.feature.moviedetail.ui.movieDetailRoute
import com.semba.modernmoviesapp.feature.moviedetail.ui.navigateToMovieDetailScreen
import com.semba.modernmoviesapp.feature.movielist.ui.movieListRoute
import com.semba.modernmoviesapp.feature.movielist.ui.navigateToMovieListScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor
)
{
    val currentNavDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentScreenDestination: ScreenDestination
        @Composable get() = when (currentNavDestination?.route) {
            movieListRoute -> ScreenDestination.MOVIE_LIST
            movieDetailRoute -> ScreenDestination.MOVIE_DETAIL
            else -> ScreenDestination.MOVIE_LIST
        }

    val isOffline = networkMonitor.isOnline
        .map ( Boolean::not )
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun navigateToScreenDestination(screenDestination: ScreenDestination, args: Map<String, String>) {
        val topLevelOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            // Avoid multiple copies of the same destination by re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true

            when (screenDestination) {
                ScreenDestination.MOVIE_LIST -> navController.navigateToMovieListScreen()
                ScreenDestination.MOVIE_DETAIL -> navController.navigateToMovieDetailScreen(args)
            }
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}