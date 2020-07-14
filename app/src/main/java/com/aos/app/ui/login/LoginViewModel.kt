package com.aos.app.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aos.app.R
import com.aos.app.ui.base.BaseViewModel
import com.aos.app.ui.login.data.LoginRepository
import com.aos.app.ui.login.data.checkResult
import com.aos.app.ui.login.data.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel(private val loginRepository: LoginRepository) : BaseViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginUiState<UserInfo?>>()
    val loginResult: LiveData<LoginUiState<UserInfo?>> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = LoginUiState(true)
            val loginRet = loginRepository.login(username, password)
            loginRet.checkResult(onSuccess = {
                _loginResult.value = LoginUiState(isSuccess = it, enableLoginButton = true)
            }, onError = {
                _loginResult.value = LoginUiState(isError = it, enableLoginButton = true)
            })
        }
    }

    suspend fun loginIO(username: String, password: String) = withContext(Dispatchers.IO) {
        // can be launched in a separate asynchronous job
//        val result = loginRepository.login(username, password)

//        ARetrofit.getApiService<ApiAuthService>().login(username, password)
    }


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}

class LoginUiState<T>(
    isLoading: Boolean = false,
    isSuccess: T? = null,
    isError: String? = null,
    val enableLoginButton: Boolean = false,
    val needLogin: Boolean = false
) : BaseViewModel.UiState<T>(isLoading, false, isSuccess, isError)