package com.example.kotlincompose.movie.detail.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.base.BaseViewModel
import com.example.base.State
import com.example.entity.data.DetailMovie
import com.example.entity.data.Movie
import com.example.repository.MovieRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class MovieDetailIntent {
    data class MovieDetail(val id: String) : MovieDetailIntent()
}

data class MovieDetailModel(
    val movieDetailState: State<DetailMovie> = State.Idle
)

class MovieDetailViewModel(
    private val movieRepository: MovieRepository = MovieRepository()
) :
    BaseViewModel<MovieDetailModel, MovieDetailIntent>(MovieDetailModel()) {
    override fun handleIntent(appIntent: MovieDetailIntent) {
        when (appIntent) {
            is MovieDetailIntent.MovieDetail -> {
                fetchMovieById(appIntent.id)
            }
        }
    }
    
    private fun fetchMovieById(id: String) = viewModelScope.launch {
        movieRepository.movieById(id).stateIn(this).collectLatest { state ->
            updateDetailMovieModel {
                it.copy(
                    movieDetailState = state
                )
            }
        }
    }
    
}