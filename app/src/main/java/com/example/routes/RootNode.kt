package com.example.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.activeElement
import com.bumble.appyx.navmodel.backstack.operation.push


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
        Children(
            navModel = backStack,
            modifier = Modifier.fillMaxSize()
        )
    }
    
    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.Main -> MainNode(buildContext) { backStack.push(navTarget) }
        }
    }
}