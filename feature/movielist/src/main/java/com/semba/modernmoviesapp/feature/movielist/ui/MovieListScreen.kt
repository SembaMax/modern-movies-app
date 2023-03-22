package com.semba.modernmoviesapp.feature.movielist.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semba.modernmoviesapp.core.common.TestTags
import com.semba.modernmoviesapp.core.design.component.ErrorView
import com.semba.modernmoviesapp.core.design.component.LoadingView
import com.semba.modernmoviesapp.core.design.navigation.ScreenDestination
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.feature.movielist.domain.MovieListScreenContract
import com.semba.modernmoviesapp.feature.movielist.ui.state.*
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import com.semba.modernmoviesapp.core.design.R as DesignR

private val MinTopBarHeight = 135.dp
private val MaxTopBarHeight = 150.dp
private val LIST_CONTENT_PADDING = 15.dp

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MovieListRoute(modifier: Modifier = Modifier, viewModel: MovieListViewModel = hiltViewModel(), navigateTo: (screenDestination: ScreenDestination, args: Map<String, String>) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val queryState by viewModel.queryState.collectAsStateWithLifecycle()

    MovieListScreen(uiState = uiState, modifier = modifier, queryState = queryState, navigateTo = navigateTo, contract = viewModel)
}

@Composable
fun MovieListScreen(uiState: MovieListUiState,
                    modifier: Modifier = Modifier,
                    queryState: String = "",
                    navigateTo: (screenDestination: ScreenDestination, args: Map<String, String>) -> Unit = {_,_->},
                    contract: MovieListScreenContract? = null) {

    val toolbarHeightRange = with(LocalDensity.current) {
        MinTopBarHeight.roundToPx()..MaxTopBarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val gridState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached = gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(0f, toolbarState.consumed)
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (available.y > 0) {
                    scope.launch {
                        animateDecay(
                            initialValue = toolbarState.height + toolbarState.offset,
                            initialVelocity = available.y,
                            animationSpec = FloatExponentialDecaySpec()
                        ) { value, velocity ->
                            toolbarState.scrollTopLimitReached = gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
                            toolbarState.scrollOffset = toolbarState.scrollOffset - (value - (toolbarState.height + toolbarState.offset))
                            if (toolbarState.scrollOffset == 0f) scope.coroutineContext.cancelChildren()
                        }
                    }
                }

                return super.onPostFling(consumed, available)
            }
        }
    }

    Box(modifier = modifier
        .nestedScroll(nestedScrollConnection)
        .background(MaterialTheme.colorScheme.background)) {
        MoviesGrid(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = toolbarState.height + toolbarState.offset }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { scope.coroutineContext.cancelChildren() }
                    )
                },
            gridState,
            uiState,
            loadMore = { contract?.loadNextPage() },
            onItemClick = { movieItem ->
                navigateTo(ScreenDestination.MOVIE_DETAIL, movieItem.toArgs())
            })

        CollapsingTopBar(modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { toolbarState.height.toDp() })
            .graphicsLayer { translationY = toolbarState.offset },
            queryState = queryState,
            onSearchClick = { contract?.onSearchClick() },
            updateQuery = {query -> contract?.updateQuery(query)})
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CollapsingTopBar(modifier: Modifier = Modifier, queryState: String, onSearchClick: () -> Unit, updateQuery: (String) -> Unit) {

    val keyboard = LocalSoftwareKeyboardController.current

    Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {

        Box(modifier = Modifier
            .fillMaxSize()) {

            Column(modifier = Modifier
                .fillMaxSize()
            ) {

                Text(text = stringResource(id = DesignR.string.search_headline),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextField(
                        value = queryState,
                        trailingIcon = { Icon(painter = painterResource(id = DesignR.drawable.ic_search), modifier = Modifier.size(30.dp), contentDescription = null, tint = Color.DarkGray) },
                        onValueChange = { updateQuery(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboard?.hide()
                                onSearchClick()
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.outlineVariant,
                            textColor = Color.DarkGray,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        singleLine = true)
                }
            }
        }
    }
}

@Composable
fun MoviesGrid(modifier: Modifier = Modifier, listState: LazyListState = rememberLazyListState(), uiState: MovieListUiState, loadMore: () -> Unit, onItemClick: (MovieItem) -> Unit) {

    val shouldLoadMore = remember {
        derivedStateOf {
            !uiState.isLoading && !uiState.limitReached && (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -100) >= (listState.layoutInfo.totalItemsCount - 3)
        }
    }

    val shouldShowError by remember {
        derivedStateOf {
            !uiState.errorMsg.isNullOrEmpty() && uiState.movieItems.isEmpty()
        }
    }

    LaunchedEffect(key1 = shouldLoadMore.value) {
        if (shouldLoadMore.value)
            loadMore()
    }

    LazyColumn(
        modifier = modifier
            .testTag(TestTags.MOVIES_GRID_TEST_TAG),
        contentPadding = PaddingValues(start = LIST_CONTENT_PADDING, end = LIST_CONTENT_PADDING, bottom = LIST_CONTENT_PADDING, top = LIST_CONTENT_PADDING),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {

        moviesGridContent(uiState.movieItems, onItemClick)

        if (uiState.isLoading) {
            item {
                LoadingItem()
            }
        }

        if (shouldShowError)
        {
            item {
                ErrorItem()
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Box(modifier = Modifier.fillMaxWidth()) {
        LoadingView(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Center),
            size = 40.dp,
            showText = false
        )
    }
}

@Composable
fun ErrorItem() {
    Box(modifier = Modifier.fillMaxWidth()) {
        ErrorView(modifier = Modifier
            .align(Alignment.Center))
    }
}

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): TopBarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(toolbarHeightRange)
    }
}

private fun LazyListScope.moviesGridContent(movies: List<MovieItem>, onItemClick: (MovieItem) -> Unit) {
    items(movies.count()) { index ->
        val animation = tween<Float>(durationMillis = 250, delayMillis = 50, easing = LinearOutSlowInEasing)
        val args = ScaleAndAlphaArgs(fromScale = 1.5f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
        val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)
        val movieItem = movies[index]
        MovieGridItem(modifier = Modifier
            .clickable { onItemClick(movieItem) }
            .graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale), item = movieItem)
    }
}

@Composable
fun scaleAndAlpha(
    args: ScaleAndAlphaArgs,
    animation: FiniteAnimationSpec<Float>
): Pair<Float, Float> {
    val transitionState = remember { MutableTransitionState(TransitionState.PLACING).apply { targetState = TransitionState.PLACED } }
    val transition = updateTransition(transitionState, label = "")
    val alpha by transition.animateFloat(transitionSpec = { animation }, label = "") { state ->
        when (state) {
            TransitionState.PLACING -> args.fromAlpha
            TransitionState.PLACED -> args.toAlpha
        }
    }
    val scale by transition.animateFloat(transitionSpec = { animation }, label = "") { state ->
        when (state) {
            TransitionState.PLACING -> args.fromScale
            TransitionState.PLACED -> args.toScale
        }
    }
    return alpha to scale
}
