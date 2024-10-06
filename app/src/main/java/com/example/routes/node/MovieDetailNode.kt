package com.example.routes.node

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.example.kotlincompose.movie.detail.MovieDetailScreen

class MovieDetailNode(
    buildContext: BuildContext,
    private val movieId: String,
) : Node(
    buildContext = buildContext,
) {
    @Composable
    override fun View(modifier: Modifier) {
        MovieDetailScreen(id = movieId, modifier)
    }
}