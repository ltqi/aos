package com.aos.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aos.app.R
import com.aos.app.databinding.ALoginBinding
import com.aos.app.ui.base.checkPermissions
import com.aos.app.toast
import com.aos.app.ui.MainActivity
import kotlinx.android.synthetic.main.a_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
//    private val loginViewModel  by viewModels<LoginViewModel> { LoginViewModelFactory() }

    private lateinit var dataBinding: ALoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions({
            toast("全部授权")
        },{
            toast("未授权权限: " + it?.toMutableSet().toString())
        })

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
        dataBinding = DataBindingUtil.setContentView<ALoginBinding>(this, R.layout.a_login)
        dataBinding.lifecycleOwner = this
//        dataBinding.vm = loginViewModel

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.isSuccess != null) {
                //Complete and destroy login activity once successful
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                loginResult.isError?.let { toast(it) }
            }
        })

//        username.afterTextChanged {
//            loginViewModel.loginDataChanged(
//                username.text.toString(),
//                password.text.toString()
//            )
//        }
//
        password.apply {
//            afterTextChanged {
//                loginViewModel.loginDataChanged(
//                    username.text.toString(),
//                    password.text.toString()
//                )
//            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
//                        loginViewModel.login(username.text.toString(), password.text.toString())
                        loginViewModel.loginMK(username.text.toString(), password.text.toString())
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
//                loginViewModel.login(username.text.toString(), password.text.toString())
                loginViewModel.loginMK(username.text.toString(), password.text.toString())
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //google建议解决内存泄漏
        finishAfterTransition()
    }

}
