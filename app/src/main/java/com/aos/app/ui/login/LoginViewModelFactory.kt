package com.aos.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aos.app.net.ApiAuthService
import com.aos.app.ui.login.data.LoginDataSource
import com.aos.app.ui.login.data.LoginRepository
import com.q.net.ARetrofit

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                    loginRepository = LoginRepository(
                        authService = ARetrofit.getApiService()
                    )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}