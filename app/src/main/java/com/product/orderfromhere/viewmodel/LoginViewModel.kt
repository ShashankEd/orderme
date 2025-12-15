package com.product.orderfromhere.viewmodel

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    // Specify the data
    private var userName = MutableLiveData("")
    private var password = MutableLiveData("")
    private var sessionToken = MutableLiveData("")

    var userNameData: LiveData<String> = userName

    var passwordData: LiveData<String> = password

    fun updateUsername(username: String) {
        this.userName.value = username
    }

    fun updatePassword(password: String) {
        this.password.value = password
    }

    val session: LiveData<String> = sessionToken

    fun updateSessionToken(token: String) {
        this.sessionToken.value = token
    }
}