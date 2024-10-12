package com.example.kotlincompose.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.base.BaseViewModel
import com.example.base.State
import com.example.entity.data.Category
import com.example.entity.data.DetailMovie
import com.example.entity.data.Movie
import com.example.repository.MovieRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class HomeIntent {
    data object PopularMovie : HomeIntent()
    data object NowPlaying : HomeIntent()
    data class DetailMovie(val id: String) : HomeIntent()
    data object Categories : HomeIntent()
}

data class HomeModel(
    val movieResponseState: State<Movie> = State.Idle,
    val nowPlayingResponseState: State<Movie> = State.Idle,
    val detailMovieState: State<DetailMovie> = State.Idle,
    val categoryResponseState: State<Category> = State.Idle,
)

class HomeViewModel(
    private val movieRepository: MovieRepository = MovieRepository()
) : BaseViewModel<HomeModel, HomeIntent>(HomeModel()) {
    
    private var hasLoadedPopularMovies = false
    private var hasLoadedNowPlayingMovies = false
    private var hasLoadedCategories = false
    
    
    override fun handleIntent(appIntent: HomeIntent) {
        when (appIntent) {
            is HomeIntent.PopularMovie -> {
                if (!hasLoadedPopularMovies) {
                    hasLoadedPopularMovies = true
                    fetchPopularMovie()
                }
            }
            
            is HomeIntent.NowPlaying -> {
                if (!hasLoadedNowPlayingMovies) {
                    hasLoadedNowPlayingMovies = true
                    fetchNowPlayingMovie()
                }
            }
            
            is HomeIntent.DetailMovie -> fetchMovieById(id = appIntent.id)
            
            is HomeIntent.Categories -> {
                println("Debug fetchCategoris AppView Model: ${hasLoadedCategories}")
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
    
    private fun fetchMovieById(id: String) = viewModelScope.launch {
        movieRepository.movieById(id = id).stateIn(this)
            .collectLatest { state ->
                updateDetailMovieModel {
                    it.copy(detailMovieState = state)
                }
            }
    }
    
    private fun fetchPopularMovie() = viewModelScope.launch {
        movieRepository.popularMovie().stateIn(this).collectLatest { state ->
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