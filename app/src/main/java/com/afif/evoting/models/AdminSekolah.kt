package com.afif.evoting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdminSekolah(
    @SerializedName("id") var id : Int?  = null,
    @SerializedName("nama_admin") var nama_admin : String?  = null,
    @SerializedName("nama_sekolah") var nama_sekolah : String?  = null,
    @SerializedName("kategori") var kategori : String?  = null,
    @SerializedName("alamat") var alamat : String?  = null,
    @SerializedName("foto") var foto : String?  = null,
    @SerializedName("email") var email : String?  = null
) : Parcelable