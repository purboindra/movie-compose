package com.example.kotlincompose

import androidx.lifecycle.viewModelScope
import com.example.base.BaseViewModel
import com.example.base.State
import com.example.entity.data.Movie
import com.example.repository.MovieRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AppIntent {
    data object PopularMovie : AppIntent()
    data object NowPlaying:AppIntent()
}

data class AppModel(
    val movieResponseState: State<Movie> = State.Idle,
    val nowPlayingResponseState: State<Movie> = State.Idle
)

class AppViewModel(
    private val movieRepository: MovieRepository = MovieRepository()
) : BaseViewModel<AppModel, AppIntent>(AppModel()) {
    override fun handleIntent(appIntent: AppIntent) {
        when (appIntent) {
            is AppIntent.PopularMovie -> fetchPopularMovie()
            is  AppIntent.NowPlaying -> fetchNowPlayingMovie()
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
        println("fetchNowPlayingMovie AppViewModel called")
        movieRepository.nowPlayingMovie().stateIn(this).collectLatest { state ->
            updateNowPLayingModel {
                it.copy(nowPlayingResponseState = state)
            }
        }
    }
}