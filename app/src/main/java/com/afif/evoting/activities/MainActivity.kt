package com.afif.evoting.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afif.evoting.R
import com.afif.evoting.models.Profile
import com.afif.evoting.utils.Utilities
import com.afif.evoting.viewmodels.PemilihanState
import com.afif.evoting.viewmodels.PemilihanViewModel
import com.afif.evoting.viewmodels.UserState
import com.afif.evoting.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var pemilihanViewModel: PemilihanViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        buttonBehavior()
        logoutButtonBehavior()
        observe()
    }


    private fun observe(){
        observeUserState()
        observePemilihanState()
        observeProfile()
        observeFlagStatus()
        observeFlagPemilihanDate()
    }

    private fun observeUserState() = userViewModel.listenUIState().observer(this, Observer { handleUserState(it) })
    private fun observePemilihanState() = pemilihanViewModel.getStatePemilihan().observer(this, Observer { handlePemilihanState(it) })
    private fun observeFlagPemilihanDate() = pemilihanViewModel.getStatusFlag().observe(this, Observer { handleFlagByDate(it) })
    private fun observeFlagStatus() = userViewModel.getFlagStatus().observe(this, Observer { handleFlagStatus(it) })
    private fun observeProfile() = userViewModel.listenToProfile().observe(this, Observer { handleProfile(it) })


    private fun handleUserState(it: UserState){
        when(it){
            is UserState.ShowToast -> toast(it.message)
        }
    }

    private fun handlePemilihanState(it: PemilihanState){
        when(it){
            is PemilihanState.ShowToast -> toast(it.message)
        }
    }

    private fun setupViewModel(){
        pemilihanViewModel = ViewModelProvider(this).get(PemilihanViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

    }

    private fun logoutButtonBehavior(){
        btn_logout.setOnClickListener {
            Utilities.clearToken(this@MainActivity)
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            this@MainActivity.finish()
        }
    }

    private fun buttonBehavior(){
        calon.setOnClickListener { startActivity(Intent(this@MainActivity, CalonActivity::class.java)) }
        hasil.setOnClickListener { startActivity(Intent(this@MainActivity, HasilActivity::class.java)) }
        profil.setOnClickListener { startActivity(Intent(this@MainActivity, ProfileActivity::class.java)) }
    }


    private fun handleFlagByDate(currentStatus : Boolean?){
        val statusInUserViewModel = userViewModel.getFlagStatus().value
        if(currentStatus == null){
            voting.setOnClickListener {
                toast("Tunggu sebentar")
            }
        }else if(currentStatus == true && (statusInUserViewModel != null && statusInUserViewModel == false)){
            voting.setOnClickListener {
                startActivity(Intent(this@MainActivity, VotingActivity::class.java))
            }
        }else if(currentStatus == false){
            voting.setOnClickListener {
                popup("Ini bukan tanggal pemilihan")
            }
        }
    }


    private fun handleFlagStatus(currentStatus : Boolean?){
        val statusInPemilihanViewModel = pemilihanViewModel.getStatusFlag().value
        if(currentStatus == null){
            voting.setOnClickListener {
                toast("Tunggu sebentar..")
            }
        }else if(currentStatus == false && (statusInPemilihanViewModel != null && statusInPemilihanViewModel == true)){
            voting.setOnClickListener {
                startActivity(Intent(this@MainActivity, VotingActivity::class.java))
            }
        }else if(currentStatus == true){
            voting.setOnClickListener {
                popup("Anda sudah pernah memilih")
            }
        }
    }

    private fun handleProfile(it: Profile?) {
        it?.let {
            nama_sekolah.text = it.id_adminsekolah.nama_sekolah
        }
    }


    private fun popup(message: String) {
        AlertDialog.Builder(this@MainActivity).apply {
            setMessage(message)
            setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        }.show()
    }


    private fun fetchProfile() = userViewModel.profile(Utilities.getToken(this@MainActivity)!!)
    private fun fetchPemilihan() = pemilihanViewModel.getPemilihan(Utilities.getToken(this@MainActivity)!!)


    override fun onResume() {
        super.onResume()
        fetchProfile()
        fetchPemilihan()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        fetchProfile()
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
