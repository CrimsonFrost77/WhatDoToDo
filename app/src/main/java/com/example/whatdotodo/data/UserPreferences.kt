// data/UserPreferences.kt

package com.example.whatdotodo.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(email: String, password: String) {
        prefs.edit()
            .putString("email", email)
            .putString("password", password)
            .apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString("email", null)
    }

    fun getUserPassword(): String? {
        return prefs.getString("password", null)
    }

    fun clearUser() {
        prefs.edit()
            .clear()
            .apply()
    }

    fun isUserLoggedIn(): Boolean {
        return getUserEmail() != null && getUserPassword() != null
    }
}
