package com.example.testemerge.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testemerge.R
import com.example.testemerge.api.MoviesApiManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


enum class LoadingState {
    Idle,
    Loading,
    Success,
    Failure
}

data class LoginViewState(
    val userName: String = "fergus_hewson",
    val userNameError: String = "",
    val password: String = "5C9khbQ2bQz3pi",
    val passwordError: String = "",
    val loadingState: LoadingState = LoadingState.Idle
)

class Interactions(
    val updateUserName: (String) -> Unit,
    val updatePassword: (String) -> Unit,
    val login: () -> Unit
)

class LoginViewModel(
    private val application: Application,
    private val moviesApiManager: MoviesApiManager
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState> = _state

    val interactions = Interactions(
        updateUserName = ::updateUsername,
        updatePassword = ::updatePassword,
        login = ::login
    )

    private fun login() {
        if (state.value.userName.isEmpty()) {
            _state.update { it.copy(userNameError = application.getString(R.string.enter_username)) }
        } else if (state.value.password.isEmpty()) {
            _state.update { it.copy(passwordError = application.getString(R.string.enter_password)) }
        } else {
            _state.update { it.copy(loadingState = LoadingState.Loading) }
            viewModelScope.launch {
                try {
                    moviesApiManager.login(_state.value.userName, _state.value.password)
                    _state.update { it.copy(loadingState = LoadingState.Success) }
                } catch (ex: Throwable) {
                    println(ex)
                    _state.update { it.copy(loadingState = LoadingState.Failure) }
                }
            }
        }
    }

    private fun updatePassword(password: String) {
        _state.update {
            it.copy(
                password = password, passwordError = ""
            )
        }
    }

    private fun updateUsername(userName: String) {
        _state.update { it.copy(userName = userName, userNameError = "") }
    }
}