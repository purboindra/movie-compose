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
    data object FetchNowPlayingMovie : AppIntent()
}

data class AppModel(
    val movieResponseState: State<Movie> = State.Idle
)

class AppViewModel(
    private val movieRepository: MovieRepository = MovieRepository()
) : BaseViewModel<AppModel, AppIntent>(AppModel()) {
    override fun handleIntent(appIntent: AppIntent) {
        when (appIntent) {
            is AppIntent.FetchNowPlayingMovie -> fetchNowPlayingMovie()
        }
    }
    
    
    private fun fetchNowPlayingMovie() = viewModelScope.launch {
        movieRepository.fetchNowPlaying().stateIn(this).collectLatest { state ->
            updateModel {
                it.copy(movieResponseState = state)
            }
        }
    }
}