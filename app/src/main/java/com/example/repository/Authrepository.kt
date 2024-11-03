package com.example.repository

import android.util.Log
import com.example.base.BaseRepository
import com.example.base.State
import com.example.entity.data.DetailMovie
import com.example.entity.data.RequestTokenResponse
import com.example.utils.baseUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

class AuthRepository : BaseRepository() {
    
    fun login(username: String, password: String): Flow<State<Any>> {
        return channelFlow {
            send(State.Loading)
            try {
                requestToken().collectLatest { requestTokenState ->
                    when (requestTokenState) {
                        is State.Success -> {
                            val token = requestTokenState.data
                            Log.d(
                                "AuthRepository Login",
                                "Success login AuthRepository requestTokenState $token"
                            )
                            createSession(
                                username,
                                password,
                                token.requestToken,
                            ).collectLatest { createSessionState ->
                                when (createSessionState) {
                                    is State.Success -> {
                                        Log.d(
                                            "AuthRepository Login",
                                            "Success login AuthRepository createSessionState $createSessionState"
                                        )
                                        send(State.Success(createSessionState))
                                    }
                                    
                                    is State.Failure -> {
                                        Log.e(
                                            "Error login AuthRepository createSessionState",
                                            "Error login AuthRepository createSessionState" + createSessionState.throwable.message.orEmpty()
                                        )
                                        send(State.Failure(createSessionState.throwable))
                                    }
                                    
                                    else -> {
                                        Log.e("Error login AuthRepository Else", "Nothing")
                                    }
                                }
                            }
                        }
                        
                        is State.Failure -> {
                            Log.e(
                                "Error login AuthRepository",
                                requestTokenState.throwable.message.orEmpty()
                            )
                            send(State.Failure(requestTokenState.throwable))
                        }
                        
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                send(State.Failure(e))
            }
        }
    }
    
    
    fun createSession(username: String, password: String, requestToken: String): Flow<State<Any>> {
        return suspend {
            postHttpResponse(
                "${baseUrl}/3/authentication/token/validate_with_login",
                body = mapOf<String, Any>(
                    "username" to username,
                    "password" to password,
                    "request_token" to requestToken
                ),
            )
        }.reduce<Any, Any> { response ->
            State.Success(response)
        }
    }
    
    fun requestToken(): Flow<State<RequestTokenResponse>> {
        return suspend {
            fetchHttpResponse("${baseUrl}/3/authentication/token/new")
        }.reduce<RequestTokenResponse, RequestTokenResponse> { response ->
            Log.d("requestToken","Response requestToken :${response}")
            State.Success(response)
        }
    }
}