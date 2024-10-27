package com.example.kotlincompose.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.base.BaseViewModel
import com.example.base.State
import com.example.entity.data.Category
import com.example.entity.data.Movie
import com.example.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class HomeIntent {
    data class PopularMovie(val genres: String? = null) : HomeIntent()
    data class NowPlaying(val genres: String? = null) : HomeIntent()
    data object Categories : HomeIntent()
}

data class HomeModel(
    val movieResponseState: State<Movie> = State.Idle,
    val nowPlayingResponseState: State<Movie> = State.Idle,
    val categoryResponseState: State<Category> = State.Idle,
)

class HomeViewModel (
    private val movieRepository: MovieRepository = MovieRepository()
) : BaseViewModel<HomeModel, HomeIntent>(HomeModel()) {

    private var hasLoadedPopularMovies = false
    private var hasLoadedNowPlayingMovies = false
    private var hasLoadedCategories = false

    private var _categoryId: MutableStateFlow<String?> = MutableStateFlow(null)

    private var _categoryIndex: MutableStateFlow<Int?> = MutableStateFlow(null)
    val categoryIndex: StateFlow<Int?> = _categoryIndex.asStateFlow()


    fun onTapCategory(id: String) {
        if (id != _categoryId.value) {
            _categoryId.update { id }
            fetchPopularMovie(id)
        }
    }

    fun onTapCategoryIndex(index: Int) {
        _categoryIndex.update { index }
    }

    override fun handleIntent(appIntent: HomeIntent) {

        when (appIntent) {
            is HomeIntent.PopularMovie -> {
                if (!hasLoadedPopularMovies) {
                    hasLoadedPopularMovies = true
                    fetchPopularMovie(appIntent.genres)
                }
            }

            is HomeIntent.NowPlaying -> {
                if (!hasLoadedNowPlayingMovies) {
                    hasLoadedNowPlayingMovies = true
                    fetchNowPlayingMovie()
                }
            }

            is HomeIntent.Categories -> {
                if (!hasLoadedCategories) {
                    hasLoadedCategories = true
                    fetchCategories()
                }
            }
        }
    }

    private fun fetchCategories() = viewModelScope.launch {
        movieRepository.categories().stateIn(this)
            .collectLatest { state ->
                updateCategoryModel {
                    it.copy(categoryResponseState = state)
                }
            }
    }

    private fun fetchPopularMovie(genres: String? = null) = viewModelScope.launch {
        movieRepository.popularMovie(genres).stateIn(this).collectLatest { state ->
            updateModel {
                it.copy(movieResponseState = state)
            }
        }
    }

    private fun fetchNowPlayingMovie() = viewModelScope.launch {
        movieRepository.nowPlayingMovie().stateIn(this).collectLatest { state ->
            updateNowPLayingModel {
                it.copy(nowPlayingResponseState = state)
            }
        }
    }
}