package com.example.kotlincompose.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.base.State
import com.example.kotlincompose.R
import com.example.kotlincompose.login.viewmodel.LoginModel
import com.example.kotlincompose.login.viewmodel.LoginViewModel
import com.example.kotlincompose.login.viewmodel.LoginViewModelIntent
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {
    
    val loginState by loginViewModel.stateLoginModel.collectAsState()
    
    val coroutineScope = rememberCoroutineScope()
    
    
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .safeContentPadding(),
            verticalArrangement = Arrangement.Center,
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Hello, Welcome back!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = loginViewModel.username,
                    onValueChange = { loginViewModel.updateUsername(it) },
                    label = {
                        Text(text = "Username", color = Color.Gray)
                    },
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(fontSize = 18.sp),
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = loginViewModel.password,
                    onValueChange = { loginViewModel.updatePassword(it) },
                    label = {
                        Text(text = "Password", color = Color.Gray)
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_visibility_24),
                            contentDescription = "Visibility"
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(fontSize = 18.sp),
                )
                Spacer(modifier = Modifier.height(12.dp))
                ElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        coroutineScope.launch {
                            loginViewModel.handleIntent(
                                LoginViewModelIntent.Login(
                                    loginViewModel.username,
                                    loginViewModel.password,
                                )
                            )
                        }
                    }) {
                    Text(
                        text = when (loginState.loginState) {
                            is State.Success -> {
                                "Success"
                            }
                            
                            is State.Loading -> {
                                "Loading"
                            }
                            
                            else -> {
                                "Log In"
                            }
                        }
                    )
                }
            }
        }
    }
}
