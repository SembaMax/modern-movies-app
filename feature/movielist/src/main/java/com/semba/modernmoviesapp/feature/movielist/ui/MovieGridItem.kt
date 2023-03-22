package com.semba.modernmoviesapp.feature.movielist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.semba.modernmoviesapp.data.model.network.MovieItem
import com.semba.modernmoviesapp.core.design.R as DesignR

const val CARD_HEIGHT = 200
const val CARD_CONTENT_PADDING = 5

@Composable
fun MovieGridItem(modifier: Modifier = Modifier, item: MovieItem) {
    Card(modifier = modifier.height(CARD_HEIGHT.dp),
        elevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp
    ),
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.surface,
        )
        , shape = RoundedCornerShape(10.dp)) {
        Row(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.4f),
                model = item.posterPath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(
                    id = DesignR.drawable.movie_poster_placeholder
                )
            )

            InfoSection(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.6f), item.title, item.releaseDate, item.adult, item.voteAverage
            )
        }
    }
}

@Composable
private fun InfoSection(modifier: Modifier = Modifier, title: String?, releaseDate: String?, adult: Boolean?, vote: Float?) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title ?: "",
            textAlign = TextAlign.Start,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = releaseDate ?: "",
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(CARD_CONTENT_PADDING.dp)) {
            if (adult == true) {
                Icon(
                    painter = painterResource(id = DesignR.drawable.ic_adult),
                    contentDescription = "adult_icon",
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.CenterStart),
                    tint = MaterialTheme.colorScheme.error
                )
            }

            Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.CenterEnd)) {
                Text(
                    text = "$vote",
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    painter = painterResource(id = DesignR.drawable.ic_star),
                    contentDescription = "vote_icon",
                    modifier = Modifier
                        .size(25.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}