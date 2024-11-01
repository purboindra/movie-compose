package com.example.kotlincompose.movie.detail.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.base.BaseViewModel
import com.example.base.State
import com.example.entity.data.DetailMovie
import com.example.entity.data.Movie
import com.example.entity.data.TicketPref
import com.example.repository.MovieRepository
import com.example.utils.PreferenceManager
import com.example.utils.prefsTicketKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
    
    private var _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    
    override fun handleIntent(appIntent: MovieDetailIntent) {
        when (appIntent) {
            is MovieDetailIntent.MovieDetail -> {
                fetchMovieById(appIntent.id)
            }
        }
    }
    
    fun hasTicket(id: String, context: Context): Boolean {
        val ticketPrefs = PreferenceManager.getString(prefsTicketKey, context) ?: return false
        val ticketList = Json.decodeFromString<MutableList<TicketPref>>(ticketPrefs)
        return ticketList.any { it ->
            it.id == id
        }
    }
    
    suspend fun buyTicket(id: String, context: Context) {
        _isLoading.value = true
        delay(1500)
        val currentMills: Long = System.currentTimeMillis()
        
        val ticketData = TicketPref(
            id = id,
            date = currentMills
        )
        
        val ticketPrefs = PreferenceManager.getString(prefsTicketKey, context)
        val updatedTicketPref = if (ticketPrefs != null) {
            val ticketList = Json.decodeFromString<MutableList<TicketPref>>(ticketPrefs)
            ticketList.add(ticketData)
            ticketList
        } else {
            mutableListOf(ticketData)
        }
        
        PreferenceManager.saveString(
            prefsTicketKey,
            Json.encodeToString(updatedTicketPref),
            context
        )
        _isLoading.value = false
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