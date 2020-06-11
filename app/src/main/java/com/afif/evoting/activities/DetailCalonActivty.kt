package com.afif.evoting.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.api.load
import com.afif.evoting.R
import com.afif.evoting.models.Calon
import kotlinx.android.synthetic.main.activity_detail_calon_activty.*

class DetailCalonActivty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_calon_activty)
        tv_ketua.text = getPassedCalon()?.ketua!!.name
        tv_wakil.text = getPassedCalon()?.wakil!!.name
        tv_visi.setHtml(getPassedCalon()?.visi.toString())
        tv_misi.setHtml(getPassedCalon()?.misi.toString())
        img_calon.load("https://evoting-osis.herokuapp.com/uploads/adminsekolah/"+getPassedCalon()?.foto)
    }
    private fun getPassedCalon() : Calon? = intent.getParcelableExtra("CALON")
}
