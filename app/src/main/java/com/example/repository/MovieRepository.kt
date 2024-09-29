package com.example.repository

import com.example.base.BaseRepository
import com.example.base.State
import com.example.entity.data.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository : BaseRepository() {
    fun fetchNowPlaying(): Flow<State<Movie>> {
        return suspend {
            fetchHttpResponse("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1")
        }.reduce<Movie, Movie> { response ->
            State.Success(response)
        }
    }
}