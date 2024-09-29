package com.example.base

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

const val accessToken =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYTM4YzYyOWEzMTY1Mjc1NjllYmM5M2NlN2JiNDIxZiIsIm5iZiI6MTcyNzQ0MjE0NS41ODU3ODksInN1YiI6IjYyY2Q0ZjkyZTgxMzFkMWRjOWYzMzliZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.fhczhFuyzZgX3iEYCttS8xnBUe7_8O1ia45JD2IIr8s"

abstract class BaseRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    
    suspend fun fetchHttpResponse(urlString: String): HttpResponse {
       try {
           val response =
               client.get(urlString) {
                   headers {
                       append("Authorization", "Bearer $accessToken")
                       append("accept", "application/json")
                   }
               }
           println("Response fetchHttpResponse: ${response.status} -- ${response.bodyAsText()}")
           return response
       }catch (e:Exception){
           println("error: ${e.message}")
           throw e
       }
    }
    
    protected inline fun <reified T, U> (suspend () -> HttpResponse).reduce(
        crossinline block: (T) -> State<U>
    ): Flow<State<U>> {
        return flow {
            val httpResponse = invoke()
         
            if (httpResponse.status.isSuccess()) {
                val data = httpResponse.body<T>()
                val dataState = block.invoke(data)
                emit(dataState)
            } else {
                val throwable = Throwable(httpResponse.bodyAsText())
                println("throwable reduce: ${throwable.message} -- ${throwable.stackTrace}")
                val state = State.Failure(throwable)
                emit(state)
            }
        }.onStart { emit(State.Loading) }.catch {
            println("error catch: ${it.message}")
            emit(State.Failure(it))
        }
    }
    
}