package com.example.kotlincompose.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.base.BaseViewModel
import com.example.base.State
import com.example.entity.data.Movie
import com.example.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class SearchViewIntent {
    data class SearchMovie(val query: String?, val page: Int?) : SearchViewIntent()
}

data class SearchMovieModel(
    val searchMovieState: State<Movie> = State.Idle
)

class SearchViewModel(private val movieRepository: MovieRepository = MovieRepository()) :
    BaseViewModel<SearchMovieModel, SearchViewIntent>(
        SearchMovieModel()
    ) {
    
    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery : StateFlow<String?> get() =  _searchQuery
    
    // StateFlow for the UI to observe the search result state
    private val _searchMovieState = MutableStateFlow<State<Movie>>(State.Idle)
    val searchMovieState: StateFlow<State<Movie>> = _searchMovieState.asStateFlow()
    
    // Function to update the search query
    fun onSearchQueryChange(query: String?) {
        _searchQuery.value = query
    }
    
    init {
        fetchSearchMovie()
    }
    
    
    override fun handleIntent(appIntent: SearchViewIntent) {
        when (appIntent) {
            is SearchViewIntent.SearchMovie -> {
                onSearchQueryChange(appIntent.query)
            }
        }
    }
    
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun fetchSearchMovie() = viewModelScope.launch {
        
        _searchQuery.debounce(300).filter { !it.isNullOrEmpty() }.distinctUntilChanged()
            .flatMapLatest { query ->
                movieRepository.search(null, query)
            }
            .collectLatest { state ->
                _searchMovieState.value = state
            }
    }
}