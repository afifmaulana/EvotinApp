package com.afif.evoting.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afif.evoting.models.Pemilihan
import com.afif.evoting.utils.SingleLiveEvent
import com.afif.evoting.webservices.ApiClient
import com.afif.evoting.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PemilihanViewModel : ViewModel(){
    private var api  = ApiClient.instance()
    private var state : SingleLiveEvent<PemilihanState> = SingleLiveEvent()
    private var pemilihan = MutableLiveData<Pemilihan>()
    private var flagStatus = MutableLiveData<Boolean?>(null)


    private fun toast(message: String) { state.value = PemilihanState.ShowToast(message) }
    private fun setLoading() { state.value = PemilihanState.IsLoading(true) }
    private fun hideLoading() { state.value = PemilihanState.IsLoading(false) }
    private fun success() { state.value = PemilihanState.Success }
    //private fun pemilihanKosong() { state.value = PemilihanState.PemilihanKosong }


    private fun setFlagStatusToNull() {
        flagStatus.value = null
    }

    private fun setFlagStatusToBool(b: Boolean){
        flagStatus.value = b
    }

    fun getPemilihan(token : String){
        setFlagStatusToNull()
        setLoading()
        api.getPemilihan(token).enqueue(object : Callback<WrappedResponse<Pemilihan>>{
            override fun onFailure(call: Call<WrappedResponse<Pemilihan>>, t: Throwable) {
                hideLoading()
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<WrappedResponse<Pemilihan>>, response: Response<WrappedResponse<Pemilihan>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        success()
                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                        val currentDate = sdf.format(Date())
                        setFlagStatusToBool(currentDate == body.data.tanggal)
                        pemilihan.postValue(body.data)
                    }else{
                        setFlagStatusToBool(false)
                        //println(body.message)
                    }
                }else{
                    setFlagStatusToBool(false)
                    //println(response.message())
                }
                hideLoading()
            }

        })
    }

    fun getStatusFlag() = flagStatus
    fun getStatePemilihan() = state
    fun getPemilihan() = pemilihan
}

sealed class PemilihanState{
    data class IsLoading(var state : Boolean = false) : PemilihanState()
    data class ShowToast(var message : String) : PemilihanState()
    object Success : PemilihanState()
    object PemilihanKosong : PemilihanState()
}