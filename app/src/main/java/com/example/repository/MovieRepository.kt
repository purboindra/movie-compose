package com.example.repository

import com.example.base.BaseRepository
import com.example.base.State
import com.example.entity.data.Category
import com.example.entity.data.DetailMovie
import com.example.entity.data.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository  : BaseRepository() {
    fun popularMovie(genres: String? = null): Flow<State<Movie>> {
        return suspend {
            var url =
                "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&include_video=false&language=en-US&page=1"
            genres?.let {
                url += "&with_genres=${genres}"
            }
            fetchHttpResponse(url)
        }.reduce<Movie, Movie> { response ->
            State.Success(response)
        }
    }

    fun nowPlayingMovie(): Flow<State<Movie>> {
        return suspend {
            val url =
                "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&include_video=false&language=en-US&page=1"

            fetchHttpResponse(url)
        }.reduce<Movie, Movie> { response -> State.Success(response) }
    }

    fun movieById(id: String): Flow<State<DetailMovie>> {
        return suspend {
            fetchHttpResponse("https://api.themoviedb.org/3/movie/${id}?language=en-US")
        }.reduce<DetailMovie, DetailMovie> { response ->
            State.Success(response)
        }
    }

    fun categories(): Flow<State<Category>> {
        return suspend {
            fetchHttpResponse("https://api.themoviedb.org/3/genre/movie/list?language=en")
        }.reduce<Category, Category> { response ->
            State.Success(response)
        }
    }

    fun search(page: Int?, query: String?): Flow<State<Movie>> {
        return suspend {

            var url = "https://api.themoviedb.org/3/search/movie?query=";

            query.let {
                url += query
            }

            url += "&include_adult=false&language=en-US"

            page.let {
                url += "&page=$page"
            }

            fetchHttpResponse(url)
        }.reduce<Movie, Movie> { response ->
            State.Success(response)
        }
    }

}