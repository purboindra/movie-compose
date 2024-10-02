package com.example.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.example.kotlincompose.MainActivity

sealed class AppRoutes {
    object Main : AppRoutes()
    object MovieDetail : AppRoutes()
}

class AppRoutesNode(
    routingSource: BackStack<AppRoutes>,
    buildContext: BuildContext
) : ParentNode<AppRoutes>(routingSource, buildContext) {
    override fun resolve(navTarget: AppRoutes, buildContext: BuildContext): Node {
        return when (navTarget) {
            is AppRoutes.Main -> node(buildContext) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Main Screen ")
                    Button(onClick = { }) {
                        Text("Navigate to Google")
                    }
                }
            }
            
            is AppRoutes.MovieDetail -> node(buildContext) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Appyx screen")
                    Button(onClick = { }) {
                        Text("Navigate to Google")
                    }
                }
            }
            
        }
    }
}