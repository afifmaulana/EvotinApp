package com.afif.evoting.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Utilities {
    companion object {
        fun getToken(c : Context) : String? {
            val s = c.getSharedPreferences("USER", MODE_PRIVATE)
            return s?.getString("TOKEN", null)
        }

        fun setToken(context: Context, token : String){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().apply {
                putString("TOKEN", token)
                apply()
            }
        }

        fun clearToken(context: Context){
            val pref = context.getSharedPreferences("USER",Context.MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidEmail(email : String) : Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword(password : String) = password.length >= 8
    }
}