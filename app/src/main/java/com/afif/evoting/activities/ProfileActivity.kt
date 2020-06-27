package com.afif.evoting.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afif.evoting.R
import com.afif.evoting.models.User
import com.afif.evoting.utils.Utilities
import com.afif.evoting.viewmodels.UserState
import com.afif.evoting.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(){

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.listenUIState().observer(this, Observer { handleUI(it) })
        userViewModel.listenToUser().observe(this, Observer { handleUser(it) })
    }

    private fun handleUI(it : UserState){
        when(it){
            is UserState.IsLoading -> {
                if (it.state){
                    pb_profile.visibility = View.VISIBLE
                }else{
                    pb_profile.visibility = View.GONE
                }
            }

            is UserState.ShowToast -> toast(it.message)
        }
    }

    private fun handleUser(it : User){
        tv_nama.text = it.name
        tv_email.text = it.email
    }

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        userViewModel.profile(Utilities.getToken(this@ProfileActivity)!!)
    }
}