package com.afif.evoting.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private var user = MutableLiveData<User>()

    fun login (email: String, password: String){
        println("email : ${email}")
        println("pass : ${password}")
        state.value = UserState.IsLoading(true)
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                state.value = UserState.IsLoading(false)
            }
            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    println("response : ${response.message()}")
                    val body = response.body()
                    if (body?.status!!){
                        println("body ${body.message}")
                        state.value = UserState.Success(body.data.token!!)
                    }else{
                        println("body salah ${body.message}")
                        state.value = UserState.ShowToast("Login Gagal")
                    }
                }else{
                    state.value = UserState.ShowToast("respone laka brooo")
                }
                state.value = UserState.IsLoading(false)
            }

        })
    }

    fun profile(token: String){
        state.value = UserState.IsLoading(true)
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                state.value = UserState.IsLoading(false)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        println("status ${data.status}")
                        user.postValue(data)
                    }else{
                        println("body ${body.message}")
                    }
                }else{
                    println("response ${response.message()}")
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

    fun listenUIState() = state
    fun listenToUser() = user

}

sealed class UserState {
    object Reset : UserState()
    data class ShowAlert(var message : String) : UserState()
    data class IsLoading(var state : Boolean) : UserState()
    data class ShowToast(var message : String) : UserState()
    data class Validate(var email : String? = null, var password : String? = null) : UserState()
    data class Success(var token : String) : UserState()
    data class SuccessVoting(var status: Boolean) : UserState()
}