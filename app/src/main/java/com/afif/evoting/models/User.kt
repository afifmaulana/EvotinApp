package com.afif.evoting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id") var id : Int?  = null,
    @SerializedName("nama_siswa") var name : String? = null,
    @SerializedName("api_token") var token : String? = null,
    @SerializedName("status") var status : Boolean = false
) : Parcelable