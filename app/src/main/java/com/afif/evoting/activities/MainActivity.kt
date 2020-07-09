package com.afif.evoting.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afif.evoting.R
import com.afif.evoting.models.Pemilihan
import com.afif.evoting.models.User
import com.afif.evoting.utils.Utilities
import com.afif.evoting.viewmodels.PemilihanState
import com.afif.evoting.viewmodels.PemilihanViewModel
import com.afif.evoting.viewmodels.UserState
import com.afif.evoting.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_voting.*

class MainActivity : AppCompatActivity() {

    private lateinit var pemilihanViewModel: PemilihanViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //isLoggedIn()
        calon.setOnClickListener { startActivity(Intent(this@MainActivity, CalonActivity::class.java)) }
        hasil.setOnClickListener { startActivity(Intent(this@MainActivity, HasilActivity::class.java)) }
        profil.setOnClickListener { startActivity(Intent(this@MainActivity, ProfileActivity::class.java)) }
        btn_logout.setOnClickListener {
            Utilities.clearToken(this@MainActivity)
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            this@MainActivity.finish()
        }
        pemilihanViewModel = ViewModelProvider(this).get(PemilihanViewModel::class.java)
        pemilihanViewModel.getPemilihan(Utilities.getToken(this@MainActivity)!!)
        pemilihanViewModel.getState().observer(this, Observer { handlePemilihan(it) })

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.listenToUser().observe(this, Observer { handleUser(it) })
    }

    private fun handleUser(it: User) {
        if (it.status) {
            voting.setOnClickListener { popup("Anda Sudah Pernah Memilih") }
        } else {
            voting.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        VotingActivity::class.java
                    )
                )
            }
        }
    }

    private fun popup(message: String) {
        AlertDialog.Builder(this@MainActivity).apply {
            setMessage(message)
            setPositiveButton("Ya") { dialog, which -> dialog.dismiss() }
        }.show()
    }

    private fun handlePemilihan(it: PemilihanState) {
        when (it) {
            is PemilihanState.Success -> voting.setOnClickListener { popup("Ini bukan tanggal voting") }
        }
    }

    override fun onResume() {
        super.onResume()
        userViewModel.profile(Utilities.getToken(this@MainActivity)!!)
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()



//    private fun isLoggedIn(){
//        if(Utilities.getToken(this@MainActivity) != null){
//            startActivity(Intent(this, LoginActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            }).also { finish() }
//        }
//    }
}
