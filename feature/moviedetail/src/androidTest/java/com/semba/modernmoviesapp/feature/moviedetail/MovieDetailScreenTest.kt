package com.semba.modernmoviesapp.feature.moviedetail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.semba.modernmoviesapp.core.common.ErrorCode
import com.semba.modernmoviesapp.core.common.TestTags
import com.semba.modernmoviesapp.core.design.theme.ModernMoviesAppTheme
import com.semba.modernmoviesapp.data.model.network.MovieGenre
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.feature.moviedetail.ui.DetailUiState
import com.semba.modernmoviesapp.feature.moviedetail.ui.MovieDetailScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var fakeUiState: DetailUiState

    @Before
    fun init() {
        fakeUiState = DetailUiState.Loading
    }

    @Test
    fun testLoadingUIState() {
        fakeUiState = DetailUiState.Loading

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieDetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.LOADING_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testFailureUIState() {
        fakeUiState = DetailUiState.Error(ErrorCode.SERVER_ERROR)

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieDetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testSuccessUIState() {
        fakeUiState = DetailUiState.Success(movieItem)

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieDetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.LOADING_TEST_TAG).assertDoesNotExist()
    }

    @Test
    fun testMovieDetailScreenDisplaysCorrectData() {
        fakeUiState = DetailUiState.Success(movieItem)

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieDetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithText("Movie5 overview").assertIsDisplayed()
        composeTestRule.onNodeWithText("2023").assertIsDisplayed()
        composeTestRule.onNodeWithText("Adventure").assertIsDisplayed()
        composeTestRule.onNodeWithText("Action").assertIsDisplayed()
        composeTestRule.onNodeWithText("Drama").assertIsDisplayed()
    }

    @Test
    fun testBackButtonDisplayedAndClickable() {

        composeTestRule.setContent {
            ModernMoviesAppTheme() {
                MovieDetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.DETAIL_BACK_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.DETAIL_BACK_TEST_TAG).assertHasClickAction()
    }

    private val movieItem = MovieItem(
        adult = false,
        id = 5,
        overview = "Movie5 overview",
        voteAverage = 4f,
        releaseDate = "2023",
        genres = listOf(
            MovieGenre(name = "Action"),
            MovieGenre(name = "Drama"),
            MovieGenre(name = "Adventure"),
        )
    )
}