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

class PemilihanViewModel : ViewModel(){
    private var api  = ApiClient.instance()
    private var state : SingleLiveEvent<PemilihanState> = SingleLiveEvent()
    private var pemilihan = MutableLiveData<Pemilihan>()

    private fun toast(message: String) { state.value = PemilihanState.ShowToast(message) }
    private fun setLoading() { state.value = PemilihanState.IsLoading(true) }
    private fun hideLoading() { state.value = PemilihanState.IsLoading(false) }
    private fun success() { state.value = PemilihanState.Success }

    fun getPemilihan(token : String){
        setLoading()
        api.getPemilihan(token).enqueue(object : Callback<WrappedResponse<Pemilihan>>{
            override fun onFailure(call: Call<WrappedResponse<Pemilihan>>, t: Throwable) {
                println(t.message)
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<Pemilihan>>, response: Response<WrappedResponse<Pemilihan>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        //val data = body.data
                        success()
                        //pemilihan.postValue(data)
                    }else{
                        println(body.message)
                    }
                }else{
                    println(response.message())
                }
                hideLoading()
            }

        })
    }

    fun getState() = state
    fun getPemilihan() = pemilihan
}

sealed class PemilihanState{
    data class IsLoading(var state : Boolean = false) : PemilihanState()
    data class ShowToast(var message : String) : PemilihanState()
    object Success : PemilihanState()
}