package com.example.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack


class RootNode(
    buildContext: BuildContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.Screen1,
        savedStateMap = buildContext.savedStateMap,
    )
) : ParentNode<NavTarget>(
    buildContext = buildContext,
    navModel = backStack,
) {
    
    
    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            is NavTarget.Screen1 -> node(buildContext) {
                Text(
                    text = "Placeholder for child 1",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            is NavTarget.Screen2 -> node(buildContext) {
                Text(
                    text = "Placeholder for child 2",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}