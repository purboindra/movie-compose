package com.example.kotlincompose.login.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.base.BaseViewModel
import com.example.base.State
import com.example.repository.AuthRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class LoginViewModelIntent {
    data class CreateSession(val username: String, val password: String, val requestToken: String) :
        LoginViewModelIntent()
    
    data object RequestToken : LoginViewModelIntent()
    data class Login(val username: String, val password: String) : LoginViewModelIntent()
}

data class LoginModel(
    val createSessionState: State<Any> = State.Idle,
    val requestTokenState: State<Any> = State.Idle,
    val loginState: State<Any> = State.Idle,
)

class LoginViewModel(private val authRepository: AuthRepository = AuthRepository()) :
    BaseViewModel<LoginModel, LoginViewModelIntent>(LoginModel()) {
    
    
    var username by mutableStateOf("")
        private set
    
    fun updateUsername(input: String) {
        username = input
    }
    
    var password by mutableStateOf("")
        private set
    
    fun updatePassword(input: String) {
        password = input
    }
    
    override fun handleIntent(appIntent: LoginViewModelIntent) {
        when (appIntent) {
            is LoginViewModelIntent.RequestToken -> requestToken()
            is LoginViewModelIntent.CreateSession -> createSession(
                appIntent.username,
                appIntent.password,
                appIntent.requestToken
            )
            
            is LoginViewModelIntent.Login -> login(appIntent.username, appIntent.password)
        }
    }
    
    private fun login(username: String, password: String) = viewModelScope.launch {
        authRepository.login(username, password)
            .collectLatest { state ->
                updateLoginModel { currentModel ->
                    currentModel.copy(loginState = state)
                }
            }
    }
    
    private fun requestToken() = viewModelScope.launch {
        authRepository.requestToken().stateIn(this).collectLatest { state ->
            updateRequestTokenModel {
                it.copy(
                    requestTokenState = state
                )
            }
        }
    }
    
    private fun createSession(username: String, password: String, requestToken: String) =
        viewModelScope.launch {
            authRepository.createSession(username, password, requestToken).stateIn(this)
                .collectLatest { state ->
                    updateCreateSessionModel {
                        it.copy(
                            createSessionState = state
                        )
                    }
                }
        }
}