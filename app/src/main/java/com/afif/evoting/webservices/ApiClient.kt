package com.afif.evoting.webservices

import com.afif.evoting.models.Calon
import com.afif.evoting.models.Hasil
import com.afif.evoting.models.Pemilihan
import com.afif.evoting.models.User
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class ApiClient {
    //companion object adalah tempat method
    //Jadi tidak usah instansiate kelas baru untuk menggunakannya
    companion object {

        private var retrofit : Retrofit? = null
        private const val ENDPOINT = "https://evoting-osis.herokuapp.com/"

        private var option = OkHttpClient.Builder().apply {
            connectTimeout(40, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }.build()

        private fun getClient() : Retrofit {
            return if(retrofit == null) {
                retrofit = Retrofit.Builder().apply {
                    baseUrl(ENDPOINT)
                    addConverterFactory(GsonConverterFactory.create())
                    client(option)
                }.build()
                retrofit!!
            }else{
                retrofit!!
            }
        }

        fun instance() = getClient().create(ApiService::class.java)

    }
}

interface ApiService {
    @FormUrlEncoded
    @POST("api/siswa/login")
    fun login(@Field("email") email : String, @Field("password") password : String):Call<WrappedResponse<User>>

    @GET("api/siswa/profile")
    fun profile(
        @Header("Authorization") token : String
    ) : Call<WrappedResponse<User>>

    @GET("api/calon")
    fun getCalon(
        @Header("Authorization") token : String
    ):Call<WrappedListResponse<Calon>>

    @GET("api/pemilihan")
    fun getPemilihan(
        @Header("Authorization") token : String
    ):Call<WrappedResponse<Pemilihan>>

    @FormUrlEncoded
    @POST("api/voting")
    fun voting(
        @Header("Authorization") token : String,
        @Field("id_calon") id_calon : Int,
        @Field("id_adminsekolah") id_adminsekolah : Int
    ) : Call<WrappedResponse<Hasil>>

    @GET("api/hasil")
    fun hasil(
        @Header("Authorization") token : String,
        @Path("id_adminsekolah") id_adminsekolah : Int
    ) : Call<WrappedListResponse<Hasil>>
}

data class WrappedResponse <T>(
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("data") var data : T
)
data class WrappedListResponse <T>(
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("data") var data : List<T>
)
