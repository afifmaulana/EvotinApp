package com.afif.evoting.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afif.evoting.models.Calon
import com.afif.evoting.utils.SingleLiveEvent
import com.afif.evoting.webservices.ApiClient
import com.afif.evoting.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalonViewModel : ViewModel() {
    private var api = ApiClient.instance()
    private var state : SingleLiveEvent<CalonState> = SingleLiveEvent()
    private var calons = MutableLiveData<List<Calon>>()

    fun getCalon(token : String){
        state.value = CalonState.IsLoading(true)
        api.getCalon(token).enqueue(object : Callback<WrappedListResponse<Calon>>{
            override fun onFailure(call: Call<WrappedListResponse<Calon>>, t: Throwable) {
                state.value = CalonState.ShowToast("OnFailure : ${t.message}")
            }

            override fun onResponse(call: Call<WrappedListResponse<Calon>>, response: Response<WrappedListResponse<Calon>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        calons.postValue(data)
                    }else{
                        state.value = CalonState.ShowToast("Tidak dapat mengambil data")
                    }
                }else{
                    state.value = CalonState.ShowToast("response tidak berhasil ${response.message()}")
                }
                state.value = CalonState.IsLoading(false)
            }

        })
    }

    fun getCalons() = calons
    fun getState() = state
}

sealed class CalonState{
    data class IsLoading(var state  : Boolean = false) : CalonState()
    data class ShowToast(var message : String) : CalonState()
}