package com.example.repository

import com.example.base.BaseRepository
import com.example.base.State
import com.example.entity.data.DetailMovie
import com.example.entity.data.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository : BaseRepository() {
    fun popularMovie(): Flow<State<Movie>> {
        return suspend {
            fetchHttpResponse("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1")
        }.reduce<Movie, Movie> { response ->
            State.Success(response)
        }
    }
    
    fun nowPlayingMovie(): Flow<State<Movie>> {
        println("nowPlayingMovie MovieRepository called")
        return suspend {
            fetchHttpResponse("https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=1")
        }.reduce<Movie, Movie> { response -> State.Success(response) }
    }
    
    fun movieById(id: String): Flow<State<DetailMovie>> {
        return suspend {
            fetchHttpResponse("https://api.themoviedb.org/3/movie/${id}?language=en-US")
        }.reduce<DetailMovie, DetailMovie> { response ->
            State.Success(response)
        }
    }
    
}