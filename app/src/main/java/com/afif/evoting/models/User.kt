package com.afif.evoting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id") var id : Int?  = null,
    @SerializedName("id_adminsekolah") var id_adminsekolah : Int?= null,
    @SerializedName("nama_siswa") var name : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("api_token") var token : String? = null,
    @SerializedName("status") var status : Boolean = false
) : Parcelable

@Parcelize
data class Profile(
    @SerializedName("id") var id : Int?  = null,
    @SerializedName("adminsekolah") var id_adminsekolah : AdminSekolah,
    @SerializedName("nama_siswa") var name : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("api_token") var token : String? = null,
    @SerializedName("status") var status : Boolean = false
) : Parcelable