package com.afif.evoting.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afif.evoting.models.Hasil
import com.afif.evoting.utils.SingleLiveEvent
import com.afif.evoting.webservices.ApiClient
import com.afif.evoting.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HasilViewModel : ViewModel(){
    private var api  = ApiClient.instance()
    private var state : SingleLiveEvent<HasilState> = SingleLiveEvent()
    private var hasils = MutableLiveData<List<Hasil>>()

    private fun setLoading() { state.value = HasilState.IsLoading(true) }
    private fun hideLoading() { state.value = HasilState.IsLoading(false) }
    private fun toast(message: String) { state.value = HasilState.ShowToast(message) }

    fun getHasil(token : String, id_admin_sekolah : String){
        setLoading()
        api.hasil(token, id_admin_sekolah.toInt()).enqueue(object : Callback<WrappedListResponse<Hasil>>{
            override fun onFailure(call: Call<WrappedListResponse<Hasil>>, t: Throwable) {
                hideLoading()
                println(t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Hasil>>, response: Response<WrappedListResponse<Hasil>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        hasils.postValue(data)
                    }else{
                        toast(body.message)
                    }
                }else{
                    toast(response.message())
                }
                hideLoading()
            }

        })
    }

    fun listenToState() = state
    fun listenToHasil() = hasils

}

sealed class HasilState{
    data class IsLoading(var state : Boolean = false) : HasilState()
    data class ShowToast(var message : String) : HasilState()
}