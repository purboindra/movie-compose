package com.example.base

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json

const val accessToken =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYTM4YzYyOWEzMTY1Mjc1NjllYmM5M2NlN2JiNDIxZiIsIm5iZiI6MTcyOTQxOTQxNC4xODU1MTcsInN1YiI6IjYyY2Q0ZjkyZTgxMzFkMWRjOWYzMzliZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.N8auHGHUwyaWceE9u8LnHYEueEhGR7lNhXv8n_cg4OE"

abstract class BaseRepository  {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    
    suspend fun postHttpResponse(urlString: String, body: Any): HttpResponse {
        try {
            val response = client.post(urlString) {
                Log.d("postHttpResponse", "Body Post Http Response: $body")
                contentType(ContentType.Application.Json)
                setBody(body)
                headers {
                    append("Authorization", "Bearer $accessToken")
                    append("accept", "application/json")
                }
            }
            return response;
        } catch (e: Exception) {
            Log.e("Error Post Http Response", "Error postHttpResponse: ${e.message}")
            throw e
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
            
            val debugResponse = mapOf<String, Any>(
                "status_code" to response.status,
                "body" to response.body(),
                "body_text" to response.bodyAsText(),
                "response" to response,
            )
            
            Log.d("fetchHttpResponse", "debugResponse: $debugResponse")
            return response
        } catch (e: Exception) {
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
                Log.d("reduce HttpResponse", "Response success: $data")
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