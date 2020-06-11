package com.afif.evoting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pemilihan(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("id_adminsekolah") var id_adminsekolah : Int? = null,
    @SerializedName("tanggal") var tanggal : String? = null,
    @SerializedName("waktu_mulai") var waktu_mulai : String? = null,
    @SerializedName("waktu_selesai") var waktu_selesai : String? = null,
    @SerializedName("tahun_ajaran") var tahun_ajaran : String? = null
) : Parcelable
