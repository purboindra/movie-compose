package com.example.routes

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.example.kotlincompose.MainActivity

sealed class AppRoutes{
    object Main: AppRoutes()
    object MovieDetail:AppRoutes()
}

class AppRoutesNode(routingSource:BackStack<AppRoutes>,
                    buildContext: BuildContext
):ParentNode<AppRoutes>(routingSource, buildContext) {
    override fun resolve(navTarget: AppRoutes, buildContext: BuildContext): Node {
       return when(navTarget){


        }
    }
}