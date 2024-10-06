package com.example.kotlincompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.example.kotlincompose.ui.theme.KotlinComposeTheme
import com.example.routes.RootNode

class MainActivity : NodeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinComposeTheme {
                NodeHost(
                    integrationPoint = appyxV1IntegrationPoint,
                ) {
                    RootNode(buildContext = it)
                }
            }
        }
    }
}
