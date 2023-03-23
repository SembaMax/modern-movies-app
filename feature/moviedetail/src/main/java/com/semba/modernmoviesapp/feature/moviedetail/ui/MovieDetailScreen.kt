package com.semba.modernmoviesapp.feature.moviedetail.ui

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.semba.modernmoviesapp.core.common.TestTags
import com.semba.modernmoviesapp.core.design.component.ErrorView
import com.semba.modernmoviesapp.core.design.component.LoadingView
import com.semba.modernmoviesapp.data.model.network.MovieGenre
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.core.design.R as DesignR

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MovieDetailRoute(modifier: Modifier = Modifier, viewModel: MovieDetailViewModel = hiltViewModel(), movieId: Int) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.getMovieDetail(movieId)
    }

    MovieDetailScreen(modifier = modifier, uiState = uiState)
}

@Composable
fun MovieDetailScreen(modifier: Modifier = Modifier, uiState: DetailUiState) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is DetailUiState.Error -> ErrorItem()
            DetailUiState.Loading -> LoadingItem()
            is DetailUiState.Success -> {
                DetailContent(movieItem = uiState.movieItem)
            }
        }

        DetailTopBar()
    }
}

@Composable
fun DetailContent(movieItem: MovieItem = MovieItem.empty()) {

    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background))
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)) {
            PosterView(modifier = Modifier
                .fillMaxWidth()
                .height(500.dp), poster = movieItem.posterPath, title = movieItem.title)
            BottomDetailsSection(modifier = Modifier, movieItem.releaseDate, movieItem.voteAverage, movieItem.runtime, movieItem.overview, movieItem.genres)
        }
    }
}

@Composable
fun DetailTopBar() {
    Box(modifier = Modifier.padding(10.dp)) {
        val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

        IconButton(onClick = { dispatcher?.onBackPressed() }, modifier = Modifier
            .testTag(TestTags.DETAIL_BACK_TEST_TAG)
            .size(35.dp)
            .align(Alignment.CenterStart)) {
            Icon(modifier = Modifier.size(40.dp), painter = painterResource(id = DesignR.drawable.ic_back), contentDescription = "", tint = MaterialTheme.colorScheme.onSecondary)
        }
    }
}

@Composable
fun PosterView(modifier: Modifier = Modifier, poster: String?, title: String?) {
    Box(modifier = modifier.clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))) {
        ImagePoster(poster)
    }
}

@Composable
fun ImagePoster(imageURL: String?) {
    AsyncImage(
        modifier = Modifier
            .fillMaxSize(),
        model = imageURL,
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun BottomDetailsSection(modifier: Modifier = Modifier, releaseDate: String?, vote: Float?, runTime: Double?, overview: String?, genres: List<MovieGenre>?) {
    Column(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        GenreChips(genres = genres?.filter { !it.name.isNullOrEmpty() } ?: listOf())
        Spacer(modifier = Modifier.height(15.dp))
        InfoLineView(releaseDate, vote, runTime)
        Spacer(modifier = Modifier.height(15.dp))
        OverviewText(overview)
    }
}

@Composable
fun OverviewText(overview: String?) {
    Text(
        text = overview ?: "",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun InfoLineView(releaseDate: String?, vote: Float?, runTime: Double?) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = releaseDate ?: "",
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = (runTime?: 0.0).toMovieTime(),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$vote",
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                painter = painterResource(id = DesignR.drawable.ic_star),
                contentDescription = "vote_icon",
                modifier = Modifier
                    .size(30.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun Double.toMovieTime(): String {
    val hours: Int = (this / 60).toInt()
    val minutes: Int = (this % 60).toInt()

    return "${hours}hr ${minutes}m"
}

@Composable
fun GenreChips(genres: List<MovieGenre>) {

    LazyRow(contentPadding = PaddingValues(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(genres.size) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            {
                Text(modifier = Modifier
                    .padding(5.dp, 5.dp, 5.dp, 5.dp), text = genres[it].name ?: "", color = MaterialTheme.colorScheme.onTertiary, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Box(modifier = Modifier.fillMaxSize()) {
        LoadingView(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Center),
            size = 50.dp,
            showText = false
        )
    }
}

@Composable
fun ErrorItem() {
    Box(modifier = Modifier.fillMaxSize()) {
        ErrorView(modifier = Modifier
            .align(Alignment.Center))
    }
}