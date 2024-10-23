package com.example.kotlincompose.search.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.update
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

    protected override fun performCleanup() {
        super.performCleanup()
    }

    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery: StateFlow<String?> get() = _searchQuery

    private val _searchMovieState = MutableStateFlow<State<Movie>>(State.Idle)
    val searchMovieState: StateFlow<State<Movie>> = _searchMovieState.asStateFlow()

    fun onSearchQueryChange(query: String?) {
        _searchQuery.value = query
    }

    fun resetSearchState() {
        _searchMovieState.value = State.Idle
        _searchQuery.value = null
    }

    override fun onCleared() {
        resetSearchState()
        super.onCleared()
    }

    init {
        observeSearchQuery()
    }

    override fun addCloseable(closeable: AutoCloseable) {
        Log.d("addCloseable","Closeable autoclose")
        super.addCloseable(closeable)
    }

    override fun handleIntent(appIntent: SearchViewIntent) {
        when (appIntent) {
            is SearchViewIntent.SearchMovie -> {
                onSearchQueryChange(appIntent.query)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.debounce(300)
                .filter { !it.isNullOrEmpty() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    movieRepository.search(1, query)
                }
                .collectLatest { state ->
                    _searchMovieState.value = state
                }
        }
    }
}