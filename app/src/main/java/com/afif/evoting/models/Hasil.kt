package com.afif.evoting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hasil(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("id_calon") var id_calon: Int? = null,
    @SerializedName("id_adminsekolah") var id_adminsekolah: Int? = null,
    @SerializedName("id_pemilihan") var id_pemilihan: Int? = null,
    @SerializedName("total") var total: Int? = null

) : Parcelable