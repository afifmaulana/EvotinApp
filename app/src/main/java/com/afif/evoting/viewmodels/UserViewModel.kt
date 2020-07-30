package com.afif.evoting.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afif.evoting.models.Profile
import com.afif.evoting.models.User
import com.afif.evoting.utils.SingleLiveEvent
import com.afif.evoting.utils.Utilities
import com.afif.evoting.webservices.ApiClient
import com.afif.evoting.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel(){
    private var api = ApiClient.instance()
    private var state : SingleLiveEvent<UserState> = SingleLiveEvent()
    private var user = MutableLiveData<User?>()
    private var profile = MutableLiveData<Profile>()
    private var flagStatus = MutableLiveData<Boolean?>(null)

    fun login (email: String, password: String){
        state.value = UserState.IsLoading(true)
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println("failure  ${t.message}")
                state.value = UserState.IsLoading(false)
            }
            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        state.value = UserState.Success(body.data.token!!)
                    }else{
                        state.value = UserState.ShowToast("Login Gagal")
                    }
                }else{
                    state.value = UserState.Alert("masukan email dan password dengan benar")
                }
                state.value = UserState.IsLoading(false)
            }
        })
    }

    private fun setFlagStatusToNull() {
        flagStatus.value = null
    }

    private fun setFlagStatusToBool(b: Boolean){
        flagStatus.value = b
    }

    private fun toast(message: String){
        state.value = UserState.ShowToast(message)
    }

    fun profile(token: String){
        setFlagStatusToNull()
        state.value = UserState.IsLoading(true)
        api.profile(token).enqueue(object : Callback<WrappedResponse<Profile>>{
            override fun onFailure(call: Call<WrappedResponse<Profile>>, t: Throwable) {
                state.value = UserState.IsLoading(false)
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<WrappedResponse<Profile>>, response: Response<WrappedResponse<Profile>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        profile.postValue(data)
                        setFlagStatusToBool(data.status)
                    }else{
                        setFlagStatusToBool(false)
                        state.value = UserState.ShowToast(body.message)
                    }
                }else{
                    setFlagStatusToBool(false)
                    state.value = UserState.ShowToast(response.message())
                }
                state.value = UserState.IsLoading(false)
            }
        })
    }

    fun validateLogin(email: String, password: String) : Boolean{
        state.value = UserState.Reset
        if (!Utilities.isValidEmail(email)){
            state.value = UserState.Validate(email = "Email tidak valid")
            return false
        }
        if (!Utilities.isValidPassword(password)){
            state.value = UserState.Validate(password = "Password tidak valid")
            return false
        }
        return true
    }

    fun updatePassword(token: String, aNewPassword: String){
        state.value = UserState.IsLoading(true)
        api.updatePassword(token, aNewPassword).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                state.value = UserState.IsLoading(false)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        state.value = UserState.Success("")
                    }else{
                        state.value = UserState.Success(body.message)
                    }
                }else{
                    state.value = UserState.Success(response.message())
                }
                state.value = UserState.IsLoading(false)
            }

        })
    }


    fun getFlagStatus() = flagStatus
    fun listenUIState() = state
    //fun listenToUser() = user
    fun listenToProfile() = profile
    //fun listenToSekolah() = user

}

sealed class UserState {
    object Reset : UserState()
    data class IsLoading(var state : Boolean) : UserState()
    data class ShowToast(var message : String) : UserState()
    data class Validate(var email : String? = null, var password : String? = null) : UserState()
    data class Success(var token : String) : UserState()
    data class SuccessVoting(var status: Boolean) : UserState()
    data class Alert(var message : String) : UserState()
}