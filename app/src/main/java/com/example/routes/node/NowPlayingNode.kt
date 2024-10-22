package com.example.routes.node

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.example.kotlincompose.movie.now_playing.NowPlayingScreen

class NowPlayingNode (buildContext: BuildContext):Node(buildContext) {
    @Composable
    override fun View(modifier: Modifier) {
        NowPlayingScreen()
        super.View(modifier)
    }
}