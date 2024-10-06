package com.example.routes

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.bumble.appyx.navmodel.backstack.BackStack


interface Navigator {
    fun navigate(navTarget: NavTarget)
    fun back()
}

val LocalNavBackStack = staticCompositionLocalOf<BackStack<NavTarget>> { error("No BackStack provided") }
