package com.example.base

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<MODEL, INTENT>(private val defaultModel: MODEL) : ViewModel() {

    // Override onCleared here
    protected override fun onCleared() {
        // Call a protected function for cleanup that subclasses can override
        performCleanup()
        super.onCleared() // Always call super to ensure proper ViewModel cleanup
    }

    // Protected function to be overridden by subclasses
    protected open fun performCleanup() {
        // Default cleanup logic (if any)
    }


    abstract fun handleIntent(appIntent: INTENT)
    
    private val mutableStateModel: MutableStateFlow<MODEL> = MutableStateFlow(defaultModel)
    val stateModel: StateFlow<MODEL> get() = mutableStateModel
    
    private val mutableStateNowPlayingModel: MutableStateFlow<MODEL> =
        MutableStateFlow(defaultModel)
    val stateNowPlayingModel: StateFlow<MODEL> get() = mutableStateNowPlayingModel
    
    // MOVIE DETAIL
    private val mutableStateDetailMovieModel: MutableStateFlow<MODEL> =
        MutableStateFlow(defaultModel)
    val stateDetailMovieModel: StateFlow<MODEL> get() = mutableStateDetailMovieModel
    
    // CATEGORY
    private val mutableStateCategoryModel: MutableStateFlow<MODEL> =
        MutableStateFlow(defaultModel)
    val stateCategoryModel: StateFlow<MODEL> get() = mutableStateCategoryModel
    
    // SEARCH
    private val mutableStateSearchMovieModel: MutableStateFlow<MODEL> =
        MutableStateFlow(defaultModel)
    val stateSearchMovieModel: StateFlow<MODEL> get() = mutableStateSearchMovieModel
    
    // AUTH
    private val mutableRequestTokenModel: MutableStateFlow<MODEL> = MutableStateFlow(defaultModel)
    val stateRequestTokenModel: StateFlow<MODEL> get() = mutableRequestTokenModel
    
    private val mutableCreateSessionModel: MutableStateFlow<MODEL> = MutableStateFlow(defaultModel)
    val stateCreateSessionModel: StateFlow<MODEL> get() = mutableCreateSessionModel
    
    private val mutableLoginStateModel: MutableStateFlow<MODEL> = MutableStateFlow(defaultModel)
    val stateLoginModel: StateFlow<MODEL> get() = mutableLoginStateModel
    
    fun updateRequestTokenModel(block: (MODEL) -> MODEL) {
        mutableRequestTokenModel.update(block)
    }
    
    fun updateCreateSessionModel(block: (MODEL) -> MODEL) {
        mutableCreateSessionModel.update(block)
    }
    
    fun updateLoginModel(block: (MODEL) -> MODEL) {
        mutableLoginStateModel.update(block)
    }
    
    fun updateSearchMovieModel(block: (MODEL) -> MODEL) {
        mutableStateSearchMovieModel.update(block)
    }
    
    fun updateCategoryModel(block: (MODEL) -> MODEL) {
        mutableStateCategoryModel.update(block)
    }
    
    fun updateDetailMovieModel(block: (MODEL) -> MODEL) {
        mutableStateDetailMovieModel.update(block)
    }
    
    fun updateNowPLayingModel(block: (MODEL) -> MODEL) {
        mutableStateNowPlayingModel.update(block)
    }
    
    fun updateModel(block: (MODEL) -> MODEL) {
        mutableStateModel.update(block)
    }
    
    fun resetState() {
        mutableStateModel.value = defaultModel
    }
}