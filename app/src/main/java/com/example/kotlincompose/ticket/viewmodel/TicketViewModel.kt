package com.example.kotlincompose.ticket.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.base.BaseViewModel
import com.example.base.State
import com.example.entity.data.Movie
import com.example.entity.data.TicketPref
import com.example.repository.MovieRepository
import com.example.utils.PreferenceManager
import com.example.utils.prefsTicketKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class TicketIntent {
    data class FetchMovie(val context: Context) : TicketIntent()
    data class DeleteTicket(val context: Context, val id: String) : TicketIntent()
}

data class TicketModel(
    val tickets: MutableList<TicketModel> = mutableListOf(),
    val movies: MutableStateFlow<State<Movie.Result>> = MutableStateFlow(State.Idle)
)

class TicketViewModel(private val movieRepository: MovieRepository = MovieRepository()) :
    BaseViewModel<TicketModel, TicketIntent>(TicketModel()) {
    
    private var _ticketState: MutableStateFlow<MutableList<TicketPref>> = MutableStateFlow(
        mutableListOf()
    )
    val ticketState: MutableStateFlow<MutableList<TicketPref>> get() = _ticketState
    
    private var _isLoadingFetchTicket: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoadingFetchTicket: MutableStateFlow<Boolean> get() = _isLoadingFetchTicket
    
    private var _movies: MutableStateFlow<State<List<Movie.Result>>> =
        MutableStateFlow(State.Idle)
    val movies: StateFlow<State<List<Movie.Result>>> get() = _movies
    
    override fun handleIntent(appIntent: TicketIntent) {
        when (appIntent) {
            is TicketIntent.FetchMovie -> {
                viewModelScope.launch {
                    fetchMovies(appIntent.context)
                }
            }
            
            is TicketIntent.DeleteTicket -> {
                viewModelScope.launch {
                    deleteTicket(appIntent.context, appIntent.id)
                }
            }
        }
    }
    
    private suspend fun deleteTicket(context: Context, id: String) {
        val ticketPrefs = PreferenceManager.getString(prefsTicketKey, context)
        val tickets = ticketPrefs?.let { Json.decodeFromString<MutableList<TicketPref>>(it) }
        val ticket = tickets?.find {
            it.id == id
        }
        if (ticket != null) {
            tickets.remove(ticket)
            PreferenceManager.saveString(prefsTicketKey, Json.encodeToString(tickets), context)
            fetchMovies(context)
        }
    }
    
    private suspend fun fetchMovies(context: Context) {
        merge(
            movieRepository.nowPlayingMovie(),
            movieRepository.popularMovie()
        ).collectLatest { state ->
            when (state) {
                is State.Success -> {
                    
                    val movies = state.data.results
                    
                    val ticketPrefs = PreferenceManager.getString(prefsTicketKey, context)
                    if (ticketPrefs != null) {
                        val tickets = Json.decodeFromString<MutableList<TicketPref>>(ticketPrefs)
                        val sortedTickets = tickets.sortedByDescending { it.date }
                        val idList = sortedTickets.map { it.id }.toSet()
                        val filteredMovies = movies.filter {
                            it.id.toString() in idList
                        }
                        _movies.emit(State.Success(filteredMovies))
                    }
                }
                
                else -> {}
            }
        }
        
        
    }
}