package com.afif.evoting.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afif.evoting.R
import com.afif.evoting.models.Hasil
import kotlinx.android.synthetic.main.item_hasil.view.*

class HasilAdapter (private var hasils : MutableList<Hasil>, private var context: Context)
    : RecyclerView.Adapter<HasilAdapter.ViewHolder>(){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(hasil: Hasil, context: Context){
            with(itemView){
                txt_calon_ketua.text = hasil.id_calon.toString()
                txt_total.text = hasil.total.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hasil, parent, false))
    }

    override fun getItemCount(): Int = hasils.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(hasils[position], context)

    fun changelist(c : List<Hasil>){
        hasils.clear()
        hasils.addAll(c)
        notifyDataSetChanged()
    }

}