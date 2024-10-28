package com.example.kotlincompose.splash

import android.widget.Space
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.navmodel.backstack.operation.replace
import com.example.routes.LocalNavBackStack
import com.example.routes.NavTarget
import com.example.utils.PreferenceManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {

    val backStack = LocalNavBackStack.current

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val username = PreferenceManager.getString("username", context)

        delay(2000)

        if (username !== null) {
            backStack.replace(NavTarget.Main)
        } else {
            backStack.replace(NavTarget.Login)
        }
    }

    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Welcome", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                CircularProgressIndicator()
            }

        }
    }
}