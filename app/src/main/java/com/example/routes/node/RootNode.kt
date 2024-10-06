package com.example.routes.node

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import com.example.routes.LocalNavBackStack
import com.example.routes.NavTarget


class RootNode(
    buildContext: BuildContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.Main,
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<NavTarget>(
    buildContext = buildContext,
    navModel = backStack
) {
    
    @Composable
    override fun View(modifier: Modifier) {
        CompositionLocalProvider(LocalNavBackStack provides backStack) {
            Children(
                navModel = backStack,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            )
        }
    }
    
    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.Main -> MainNode(buildContext) { backStack.push(navTarget) }
            is NavTarget.MovieDetail -> {
                val movieId = navTarget.id
                MovieDetailNode(buildContext, movieId = movieId)
            }
        }
    }
}