package com.afif.evoting.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Calon(
    @SerializedName("id") var id : Int?  = null,
    @SerializedName("visi") var visi : String? = null,
    @SerializedName("misi") var misi : String? = null,
    @SerializedName("foto") var foto : String? = null,
    @SerializedName("ketua") var ketua : User,
    @SerializedName("wakil") var wakil : User,
    @SerializedName("adminsekolah") var adminsekolah : AdminSekolah
) : Parcelable

