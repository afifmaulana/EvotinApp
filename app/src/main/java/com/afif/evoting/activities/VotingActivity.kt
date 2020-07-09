package com.afif.evoting.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afif.evoting.R
import com.afif.evoting.adapters.VotingAdapter
import com.afif.evoting.utils.Utilities
import com.afif.evoting.viewmodels.CalonState
import com.afif.evoting.viewmodels.CalonViewModel
import com.afif.evoting.viewmodels.VotingState
import com.afif.evoting.viewmodels.VotingViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_voting.*
import kotlinx.android.synthetic.main.item_voting.*

class VotingActivity : AppCompatActivity() {

    private lateinit var calonViewModel: CalonViewModel
    private lateinit var votingViewModel : VotingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)
        votingViewModel = ViewModelProvider(this).get(VotingViewModel::class.java)
        rv_voting.apply {
            layoutManager = LinearLayoutManager(this@VotingActivity)
            adapter = VotingAdapter(mutableListOf(), this@VotingActivity, votingViewModel)
        }
        calonViewModel = ViewModelProvider(this).get(CalonViewModel::class.java)
        calonViewModel.getCalon(Utilities.getToken(this@VotingActivity)!!)
        calonViewModel.getCalons().observe(this, Observer {
            rv_voting.adapter?.let {adapter ->
                if (adapter is VotingAdapter){
                    adapter.changelist(it)
                }
            }
        })
        calonViewModel.getState().observer(this, Observer { handleui(it) })
        votingViewModel.getState().observer(this, Observer { handleUIVoting(it) })
    }

    private fun handleUIVoting(it : VotingState){
        when(it){
            //is VotingState.IsLoading -> btn_voting.isEnabled = !it.state
            //is VotingState.ShowToast -> toast(it.message)
            is VotingState.Success -> {
                toast(it.message)
                finish()
            }
        }
    }

    private fun handleui(it : CalonState){
        when(it){
            is CalonState.IsLoading -> {
                if (it.state){
                    pb_voting.isIndeterminate = true
                    pb_voting.visibility = View.VISIBLE
                }else{
                    pb_voting.isIndeterminate = false
                    pb_voting.visibility = View.GONE
                }
            }
            is CalonState.ShowToast -> toast(it.message)

        }
    }

    private fun toast(message : String) = Toast.makeText(this@VotingActivity, message, Toast.LENGTH_LONG).show()
}
