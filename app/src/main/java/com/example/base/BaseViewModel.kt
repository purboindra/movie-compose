package com.example.base

import androidx.lifecycle.ViewModel
import com.example.kotlincompose.AppIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<MODEL, INTENT>(private val defaultModel: MODEL) : ViewModel() {
    
    private val mutableStateModel: MutableStateFlow<MODEL> = MutableStateFlow(defaultModel)
    val stateModel: StateFlow<MODEL> get() = mutableStateModel
    
    private val mutableStateNowPlayingModel: MutableStateFlow<MODEL> = MutableStateFlow(defaultModel)
    val stateNowPlayingModel: StateFlow<MODEL> get() = mutableStateNowPlayingModel
    
    fun updateNowPLayingModel(block: (MODEL) -> MODEL) {
        println("updateNowPLayingModel BaseViewModel called")
        mutableStateNowPlayingModel.update(block)
    }
    
    abstract fun handleIntent(appIntent: INTENT)
    
    fun updateModel(block: (MODEL) -> MODEL) {
        mutableStateModel.update(block)
    }
    
    fun resetState() {
        mutableStateModel.value = defaultModel
    }
}