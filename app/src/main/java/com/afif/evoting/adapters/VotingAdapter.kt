package com.afif.evoting.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.afif.evoting.R
import com.afif.evoting.models.Calon
import com.afif.evoting.utils.Utilities
import com.afif.evoting.viewmodels.VotingViewModel
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_voting.view.*

class VotingAdapter(
    private var calons: MutableList<Calon>,
    private var context: Context,
    private var votingViewModel: VotingViewModel
) : RecyclerView.Adapter<VotingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(calon: Calon, context: Context, votingViewModel: VotingViewModel) {
            with(itemView) {
                iv_foto_calon.load(calon.foto)
                tv_calon_ketua.text = calon.ketua.name
                tv_calon_wakil.text = calon.wakil.name

                btn_voting.setOnClickListener {
                    AlertDialog.Builder(context).apply {
                        setMessage("apakah anda yakin ingin memilih calon ini?")
                        setPositiveButton("Ya") { dialog, which ->
                            votingViewModel.voting(Utilities.getToken(context)!!, calon.id.toString(), calon.adminsekolah.id.toString())
                        }
                        setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
                    }.create().show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_voting, parent, false))
    }

    override fun getItemCount(): Int = calons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(calons[position], context, votingViewModel)

    fun changelist(c: List<Calon>) {
        calons.clear()
        calons.addAll(c)
        notifyDataSetChanged()
    }
}