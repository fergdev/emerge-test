package com.example.testemerge.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.testemerge.R
import com.example.testemerge.api.MovieDto
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel(), navigateHome: () -> Unit) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.movies_app)) },
            actions = {
                IconButton(onClick = { viewModel.interactions.logout() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.logout)
                    )
                }
            }
        )
        MultiChoiceSegmentedButtonRow(space = 16.dp) {
            SegmentedButton(
                checked = state.shownTab == ShownTab.Popular,
                onCheckedChange = {
                    if (state.shownTab != ShownTab.Popular) {
                        viewModel.interactions.showPopular()
                    }
                },
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(text = stringResource(R.string.popular))
            }
            SegmentedButton(
                checked = state.shownTab == ShownTab.Favourites,
                onCheckedChange = {
                    if (state.shownTab != ShownTab.Favourites) {
                        viewModel.interactions.showFavourite()
                    }
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = stringResource(R.string.favourites))
            }
        }
        when (state.loadingState) {
            HomeLoadingState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            HomeLoadingState.Loaded -> {
                Movies(state.movies, state.shownTab == ShownTab.Popular) {
                    if (state.shownTab == ShownTab.Popular) {
                        viewModel.interactions.addFavorite(it)
                    } else {
                        viewModel.interactions.removeFavorite(it)
                    }
                }
            }

            HomeLoadingState.Error -> {
                Text(
                    text = stringResource(R.string.loading_movies_error),
                    color = MaterialTheme.colorScheme.error
                )
            }

            HomeLoadingState.LoggedOut -> {
                navigateHome()
            }
        }
    }
}

@Composable
fun Movies(movies: List<MovieDto>, showPlus: Boolean, update: (MovieDto) -> Unit) {
    if (movies.isEmpty()) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = stringResource(R.string.no_movies)
        )
    } else {
        LazyColumn {
            items(movies) {
                ListItem(
                    headlineContent = { Text(text = it.title) },
                    supportingContent = { Text(text = it.details) },
                    trailingContent = {
                        IconButton(onClick = { update(it) }) {
                            Icon(
                                imageVector = if (showPlus) Icons.Default.Add else Icons.Default.Delete,
                                contentDescription = stringResource(R.string.add_to_favourites)
                            )
                        }
                    },
                )
            }
        }
    }
}