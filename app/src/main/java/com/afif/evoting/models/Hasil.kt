package com.afif.evoting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hasil(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("calon") var calon: Calon,
    @SerializedName("adminsekolah") var adminsekolah: AdminSekolah,
    @SerializedName("pemilihan") var id_pemilihan: Int? = null,
    @SerializedName("total") var total: Int? = null

) : Parcelable