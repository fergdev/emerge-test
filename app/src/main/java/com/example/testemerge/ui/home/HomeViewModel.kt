package com.example.testemerge.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testemerge.api.MovieDto
import com.example.testemerge.api.MoviesApiManager
import com.example.testemerge.api.MoviesDto
import com.example.testemerge.ui.login.Interactions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class HomeLoadingState {
    Loading,
    Loaded,
    Error,
    LoggedOut
}

enum class ShownTab {
    Popular,
    Favourites
}

data class HomeScreenState(
    val loadingState: HomeLoadingState = HomeLoadingState.Loading,
    val shownTab: ShownTab = ShownTab.Popular,
    val movies: List<MovieDto> = mutableListOf()
)

data class HomeInteractions(
    val logout: () -> Unit,
    val showPopular: () -> Unit,
    val showFavourite: () -> Unit,
    val addFavorite: (MovieDto) -> Unit,
    val removeFavorite: (MovieDto) -> Unit
)


class HomeViewModel(application: Application, private val moviesApiManager: MoviesApiManager) :
    AndroidViewModel(application) {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state

    val interactions = HomeInteractions(
        logout = ::logout,
        showFavourite = ::showFavourite,
        showPopular = ::showPopular,
        addFavorite = ::addFavorite,
        removeFavorite = ::removeFavorite
    )

    private fun removeFavorite(movieDto: MovieDto) {
        viewModelScope.launch {
            try {
                moviesApiManager.removeFavorite(movieDto)
                _state.update { it.copy(movies = it.movies.filter { it.id != movieDto.id }) }
            } catch (ex: Throwable) {
                // no-op
            }
        }
    }

    private fun addFavorite(movieDto: MovieDto) {
        viewModelScope.launch {
            try {
                moviesApiManager.addFavorite(movieDto)
            } catch (ex: Throwable) {
                // no-op
            }
        }
    }

    private fun showPopular() {
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        loadingState = HomeLoadingState.Loading,
                        shownTab = ShownTab.Popular
                    )
                }
                val latest = moviesApiManager.popular()
                _state.update {
                    it.copy(
                        loadingState = HomeLoadingState.Loaded,
                        movies = latest
                    )
                }
            } catch (ex: Throwable) {
                _state.update { it.copy(loadingState = HomeLoadingState.Error) }
            }
        }
    }

    private fun showFavourite() {
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        loadingState = HomeLoadingState.Loading,
                        shownTab = ShownTab.Favourites
                    )
                }
                val latest = moviesApiManager.favourite()
                _state.update {
                    it.copy(
                        loadingState = HomeLoadingState.Loaded,
                        movies = latest
                    )
                }
            } catch (ex: Throwable) {
                _state.update { it.copy(loadingState = HomeLoadingState.Error) }
            }
        }
    }

    private fun logout() {
        moviesApiManager.logout()
        _state.update { it.copy(loadingState = HomeLoadingState.LoggedOut) }
    }

    init {
        viewModelScope.launch {
            try {
                val latest = moviesApiManager.popular()
                _state.update {
                    it.copy(
                        loadingState = HomeLoadingState.Loaded,
                        movies = latest
                    )
                }
            } catch (ex: Throwable) {
                _state.update { it.copy(loadingState = HomeLoadingState.Error) }
            }
        }
    }
}