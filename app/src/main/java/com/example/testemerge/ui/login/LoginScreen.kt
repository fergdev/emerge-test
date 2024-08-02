package com.example.testemerge.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.testemerge.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    navigateLoggedIn: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val state = viewModel.state.collectAsState()
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(R.string.movies_app)
            )
            OutlinedTextField(
                label = { Text(text = stringResource(R.string.username)) },
                value = state.value.userName,
                onValueChange = viewModel.interactions.updateUserName,
                isError = state.value.userNameError.isNotEmpty(),
                supportingText = if (state.value.userNameError.isNotEmpty()) {
                    { Text(text = state.value.userNameError) }
                } else {
                    null
                }
            )

            OutlinedTextField(
                label = { Text(text = stringResource(R.string.password)) },
                value = state.value.password,
                onValueChange = viewModel.interactions.updatePassword,
                visualTransformation = PasswordVisualTransformation(),
                isError = state.value.passwordError.isNotEmpty(),
                supportingText = if (state.value.passwordError.isNotEmpty()) {
                    { Text(text = state.value.passwordError) }
                } else {
                    null
                }
            )
            if (state.value.loadingState != LoadingState.Loading) {
                Button(onClick = { viewModel.interactions.login() }) {
                    Text(text = stringResource(R.string.login))
                }
            } else if (state.value.loadingState == LoadingState.Loading) {
                CircularProgressIndicator()
            }
            if (state.value.loadingState == LoadingState.Success) {
                navigateLoggedIn()
            }
            if (state.value.loadingState == LoadingState.Failure) {
                Text(
                    text = stringResource(R.string.login_error),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

