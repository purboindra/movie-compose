package com.example.base

import androidx.lifecycle.ViewModel
import com.example.kotlincompose.AppIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<MODEL, INTENT>(private val defaultModel: MODEL) : ViewModel() {
    
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