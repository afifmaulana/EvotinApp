package com.afif.evoting.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afif.evoting.R
import com.afif.evoting.utils.Utilities
import com.afif.evoting.viewmodels.UserState
import com.afif.evoting.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.listenUIState().observer(this@LoginActivity, Observer { handleState(it) })

        btn_login.setOnClickListener{
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            //toast(email)
            //userViewModel.validateLogin(email, password)
            if (userViewModel.validateLogin(email, password)){
                userViewModel.login(email, password)
            }
        }
    }

    private fun toast(message: String) = Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
    private fun setErrorEmail(err : String?) { in_email.error = err}
    private fun setErrorPassword(err : String?) { in_password.error = err}
    private fun handleState(it: UserState){
        when(it){
            is UserState.Alert -> showAlert(it.message)
            is UserState.ShowToast -> toast(it.message)
            is UserState.IsLoading -> {
                if (it.state){
                    btn_login.isEnabled = false
                    loading.isIndeterminate = false
                    loading.visibility = View.VISIBLE
                }else{
                    btn_login.isEnabled = true
                    loading.isIndeterminate = true
                    loading.visibility = View.GONE
                }
            }
            is UserState.Reset -> {
                setErrorEmail(null)
                setErrorPassword(null)
            }
            is UserState.Validate -> {
                it.email?.let { e -> setErrorEmail(e) }
                it.password?.let { e -> setErrorPassword(e) }
            }
            is UserState.Success -> {
                Utilities.setToken(this@LoginActivity, "Bearer ${it.token}")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }
    private fun showAlert(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("Mengerti") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    override fun onResume() {
        super.onResume()
        if (Utilities.getToken(this@LoginActivity) != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

}
