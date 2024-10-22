package com.example.kotlincompose.login

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.bumble.appyx.navmodel.backstack.operation.push
import com.example.base.State
import com.example.entity.data.Error
import com.example.kotlincompose.R
import com.example.kotlincompose.login.viewmodel.LoginViewModel
import com.example.kotlincompose.login.viewmodel.LoginViewModelIntent
import com.example.routes.LocalNavBackStack
import com.example.routes.NavTarget
import com.example.utils.PreferenceManager
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {

    val loginState by loginViewModel.stateLoginModel.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    val backStack = LocalNavBackStack.current

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var showPassword by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar( backgroundColor = Color.Red ,snackbarData = data)
            }
        }

    ) { paddingValues ->
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
                    onValueChange = {
                        loginViewModel.updateUsername(it)
                        usernameError = if (it.isBlank()) "Username cannot be empy" else null
                    },
                    label = {
                        Text(text = "Username", color = Color.Gray)
                    },
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(fontSize = 18.sp),
                )

                if (usernameError != null) {
                    Text(
                        text = usernameError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = loginViewModel.password,
                    onValueChange = {
                        loginViewModel.updatePassword(it)
                        passwordError = if (it.isBlank()) "Password cannot be empy" else null
                    },
                    label = {
                        Text(text = "Password", color = Color.Gray)
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                showPassword = !showPassword
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = if (!showPassword) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                                contentDescription = "Visibility"
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(fontSize = 18.sp),
                )

                if (passwordError != null) {
                    Text(
                        text = passwordError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                ElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = loginState.loginState != State.Loading,
                    onClick = {
                        if (loginViewModel.username.isBlank()) {
                            usernameError = "Username cannot be empty"
                        }

                        if (loginViewModel.password.isBlank()) {
                            passwordError = "Password cannot be empty"
                        }

                        if (usernameError == null || passwordError == null) {
                            keyboardController?.hide()
                            coroutineScope.launch {
                                loginViewModel.handleIntent(
                                    LoginViewModelIntent.Login(
                                        loginViewModel.username,
                                        loginViewModel.password,
                                    )
                                )
                            }
                        }
                    }) {
                    if (loginState.loginState is State.Loading) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = "Log In"
                        )
                    }

                    LaunchedEffect(loginState.loginState) {
                        when (loginState.loginState) {
                            is State.Failure -> {
                                val error = (loginState.loginState as State.Failure).throwable
                                val message = error.message
                                Log.d("Error Message", message ?: "")
                                if (message != null) {
                                    val parseMessage =
                                        Json.decodeFromString<Error>(message)
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        parseMessage.statusMessage
                                    )
                                } else {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "Unkown Error"
                                    )
                                }
                            }

                            is State.Success -> {
                                PreferenceManager.saveString(
                                    "username",
                                    loginViewModel.username,
                                    context = context
                                )
                                backStack.push(NavTarget.Main)
                            }

                            else -> {}
                        }


                    }

                }
            }
        }
    }
}
