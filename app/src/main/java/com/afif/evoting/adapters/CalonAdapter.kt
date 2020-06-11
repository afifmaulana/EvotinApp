package com.afif.evoting.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.afif.evoting.R
import com.afif.evoting.activities.DetailCalonActivty
import com.afif.evoting.models.Calon
import kotlinx.android.synthetic.main.item_calon.view.*

class CalonAdapter(private var calons : MutableList<Calon>, private var context: Context)
    : RecyclerView.Adapter<CalonAdapter.ViewHolder>(){

    class ViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){
        fun bind(calon : Calon, context: Context){
            with(itemView){
                iv_foto_calon.load("https://evoting-osis.herokuapp.com/uploads/adminsekolah/"+calon.foto)
                tv_calon_ketua.text = calon.ketua.name
                tv_calon_wakil.text = calon.wakil.name
                setOnClickListener {
                    context.startActivity(Intent(context, DetailCalonActivty::class.java).apply {
                        putExtra("CALON", calon)
                    })
                    //Toast.makeText(context, calon.ketua.name, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_calon, parent, false))
    }

    override fun getItemCount(): Int = calons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(calons[position], context)

    fun changelist(c: List<Calon>){
        calons.clear()
        calons.addAll(c)
        notifyDataSetChanged()
    }
}
