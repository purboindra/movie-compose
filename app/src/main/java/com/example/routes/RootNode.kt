package com.example.routes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.example.kotlincompose.MainScreen


class RootNode(
    buildContext: BuildContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.Child1,
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<NavTarget>(
    buildContext = buildContext,
    navModel = backStack
) {
    
    @Composable
    override fun View(modifier: Modifier) {
        MainScreen()
    }
    
    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.Child1 -> node(buildContext) { Text(text = "Placeholder for child 1") }
            NavTarget.Child2 -> node(buildContext) { Text(text = "Placeholder for child 2") }
            NavTarget.Child3 -> MovieNode(buildContext)
        }
    }
}