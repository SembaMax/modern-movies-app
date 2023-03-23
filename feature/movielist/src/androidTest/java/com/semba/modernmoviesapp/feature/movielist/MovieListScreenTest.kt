package com.semba.modernmoviesapp.feature.movielist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.semba.modernmoviesapp.core.common.TestTags
import com.semba.modernmoviesapp.core.design.theme.ModernMoviesAppTheme
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.feature.movielist.ui.MovieListScreen
import com.semba.modernmoviesapp.feature.movielist.ui.state.MovieListUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.semba.modernmoviesapp.core.design.R as DesignR

class MovieListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var fakeUiState: MovieListUiState

    @Before
    fun init() {
        fakeUiState = MovieListUiState()
    }

    @Test
    fun testLoadingUIState() {
        fakeUiState = fakeUiState.copy(isLoading = true)

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieListScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.LOADING_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testFailureUIState() {
        fakeUiState = fakeUiState.copy(errorMsg = "Error")

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieListScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testSuccessUIState() {
        fakeUiState = fakeUiState.copy(movieItems = movies)

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieListScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.LOADING_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.MOVIES_GRID_TEST_TAG).assertExists()
    }

    @Test
    fun testListDisplaysCorrectData() {
        fakeUiState = fakeUiState.copy(movieItems = movies)

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieListScreen(uiState = fakeUiState)
            }
        }

        val firstItem = composeTestRule.onNodeWithTag(TestTags.MOVIES_GRID_TEST_TAG).onChildAt(0)
        val secondItem = composeTestRule.onNodeWithTag(TestTags.MOVIES_GRID_TEST_TAG).onChildAt(1)

        firstItem.assertIsDisplayed()
        firstItem.assert(hasText("Movie1"))
        firstItem.assert(hasText("2020"))

        secondItem.assertIsDisplayed()
        secondItem.assert(hasText("Movie2"))
        secondItem.assert(hasText("2023"))
    }

    @Test
    fun testTopBarIsDisplayedOnSuccessUIState() {

        val headerTitle = composeTestRule.activity.getString(DesignR.string.search_headline)

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieListScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithText(headerTitle).assertIsDisplayed()
    }

    private val movies = listOf(
        MovieItem(
            adult = false,
            id = 1,
            title = "Movie1",
            voteAverage = 4f,
            releaseDate = "2020"
        ),
        MovieItem(
            adult = false,
            id = 2,
            title = "Movie2",
            voteAverage = 4f,
            releaseDate = "2023"
        ),
        MovieItem(
            adult = false,
            id = 3,
            title = "Movie3",
            voteAverage = 4f,
            releaseDate = "2022"
        ),
        MovieItem(
            adult = false,
            id = 4,
            title = "Movie4",
            voteAverage = 4f,
            releaseDate = "2022"
        ),
        MovieItem(
            adult = false,
            id = 5,
            title = "Movie5",
            voteAverage = 4f,
            releaseDate = "2023"
        ),
    )

}