package com.product.orderfromhere.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.product.orderfromhere.model.datastore.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel (application: Application): AndroidViewModel(application) {
    // Specify the data
    private val tokenDataStore = DataStoreManager(application)
    private var userName = MutableLiveData("")
    private var password = MutableLiveData("")
    private var _sessionTokenInStore = MutableStateFlow<String?> (null)

    var userNameData: LiveData<String> = userName

    var passwordData: LiveData<String> = password

    var sessionTokenInStore: StateFlow<String?> = _sessionTokenInStore

    fun updateUsername(username: String) {
        this.userName.value = username
    }

    fun updatePassword(password: String) {
        this.password.value = password
    }


    val sessionFromStore = tokenDataStore.sessionToken
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    fun updateSessionToken(token: String) {
        viewModelScope.launch {
            tokenDataStore.saveSessionToken(token)
        }
    }

    fun deleteSessionToken() {
        viewModelScope.launch {
            tokenDataStore.deleteSessionToken()
        }
    }

    fun getSessionFromStore() {

    }
}