package com.afif.evoting.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afif.evoting.R
import com.afif.evoting.adapters.CalonAdapter
import com.afif.evoting.utils.Utilities
import com.afif.evoting.viewmodels.CalonState
import com.afif.evoting.viewmodels.CalonViewModel
import kotlinx.android.synthetic.main.activity_calon.*

class CalonActivity : AppCompatActivity() {
    private lateinit var calonViewModel: CalonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calon)
        rv_calon.apply {
            layoutManager = LinearLayoutManager(this@CalonActivity)
            adapter = CalonAdapter(mutableListOf(), this@CalonActivity)
        }
        calonViewModel = ViewModelProvider(this).get(CalonViewModel::class.java)
        calonViewModel.getCalon(Utilities.getToken(this@CalonActivity)!!)
        calonViewModel.getCalons().observe(this, Observer {
            rv_calon.adapter?.let {adapter ->
                if (adapter is CalonAdapter){
                    adapter.changelist(it)
                    for (x in it){
                        x.foto
                    }
                }
            }
        })
        calonViewModel.getState().observer(this, Observer { handleui(it) })

    }

    private fun handleui(it : CalonState){
        when(it){
            is CalonState.IsLoading -> {
                if (it.state){
                    pb_calon.isIndeterminate = true
                    pb_calon.visibility = View.VISIBLE
                }else{
                    pb_calon.isIndeterminate = false
                    pb_calon.visibility = View.GONE
                }
            }
            is CalonState.ShowToast -> toast(it.message)
        }
    }

    private fun toast(message : String) = Toast.makeText(this@CalonActivity, message, Toast.LENGTH_LONG).show()
}
