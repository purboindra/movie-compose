package com.example.routes

import androidx.compose.runtime.staticCompositionLocalOf
import com.bumble.appyx.navmodel.backstack.BackStack

val LocalNavBackStack = staticCompositionLocalOf<BackStack<NavTarget>> { error("No BackStack provided") }
