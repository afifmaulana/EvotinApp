package com.afif.evoting.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afif.evoting.R
import com.afif.evoting.adapters.HasilAdapter
import com.afif.evoting.models.User
import com.afif.evoting.utils.Utilities
import com.afif.evoting.viewmodels.HasilState
import com.afif.evoting.viewmodels.HasilViewModel
import com.afif.evoting.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_hasil.*

class HasilActivity : AppCompatActivity() {

    private lateinit var hasilViewModel : HasilViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil)
        rv_hasil.apply {
            adapter = HasilAdapter(mutableListOf(), this@HasilActivity)
            layoutManager = LinearLayoutManager(this@HasilActivity)
        }

        hasilViewModel = ViewModelProvider(this).get(HasilViewModel::class.java)


        hasilViewModel.listenToState().observer(this, Observer { handleUI(it) })
        hasilViewModel.listenToHasil().observe(this, Observer {
            rv_hasil.adapter?.let {adapter ->
                if (adapter is HasilAdapter){
                    adapter.changelist(it)
                }
            }
        })
    }

    private fun handleUI(it : HasilState){
        when(it){
            is HasilState.IsLoading->{
                if (it.state){
                    pb_hasil.visibility = View.VISIBLE
                    pb_hasil.isIndeterminate = true
                }else{
                    pb_hasil.visibility = View.GONE
                    pb_hasil.isIndeterminate = false
                }
            }

            is HasilState.ShowToast -> toast(it.message)
        }
    }

    private fun toast(m : String) = Toast.makeText(this@HasilActivity, m, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        hasilViewModel.getHasil(Utilities.getToken(this@HasilActivity)!!)
    }
}
