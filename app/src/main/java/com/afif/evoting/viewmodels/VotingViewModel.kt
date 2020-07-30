package com.afif.evoting.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afif.evoting.models.Hasil
import com.afif.evoting.utils.SingleLiveEvent
import com.afif.evoting.webservices.ApiClient
import com.afif.evoting.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VotingViewModel : ViewModel(){
    private var api  = ApiClient.instance()
    private var state : SingleLiveEvent<VotingState> = SingleLiveEvent()

    private fun toast(message: String) { state.value = VotingState.ShowToast(message) }
    private fun setLoading() { state.value = VotingState.IsLoading(true) }
    private fun hideLoading() { state.value = VotingState.IsLoading(false) }
    private fun success(message: String) { state.value = VotingState.Success(message) }

    fun voting(token : String, id_calon : String, id_adminsekolah : String){
        setLoading()
        api.voting(token, id_calon.toInt(), id_adminsekolah.toInt()).enqueue(object : Callback<WrappedResponse<Hasil>>{
            override fun onFailure(call: Call<WrappedResponse<Hasil>>, t: Throwable) {
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<Hasil>>, response: Response<WrappedResponse<Hasil>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        //toast("terima kasih anda telah memilih")
                        success("terima kasih anda telah memilih")
                    }else{
                        //println(body.message)
                    }
                }else{
                    //println(response.message())
                }
                hideLoading()
            }

        })
    }

    fun getState() = state
}
sealed class VotingState{
    data class IsLoading(var state : Boolean = false) : VotingState()
    data class ShowToast(var message : String) : VotingState()
    data class Success(var message: String) : VotingState()
}