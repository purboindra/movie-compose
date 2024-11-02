package com.example.kotlincompose.main

import androidx.lifecycle.ViewModel
import com.example.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class MainIntent {
    data class OnChangeBottomNavbar(val index: Int) : MainIntent()
}

data class MainModel(
    val selectedItem: Int = 0
)

class MainViewModel() : BaseViewModel<MainModel, MainIntent>(MainModel()) {
    
    private val _state = MutableStateFlow(MainModel())
    val state: StateFlow<MainModel> get() = _state
    
    override fun handleIntent(appIntent: MainIntent) {
        when (appIntent) {
            is MainIntent.OnChangeBottomNavbar -> {
                updateState {
                    it.copy(selectedItem = appIntent.index)
                }
            }
        }
    }
    
    private fun updateState(newState: (MainModel) -> MainModel) {
        _state.value = newState(_state.value)
    }
}