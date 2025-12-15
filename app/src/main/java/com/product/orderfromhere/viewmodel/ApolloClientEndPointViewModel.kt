package com.product.orderfromhere.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ApolloViewModel : ViewModel() {
    private var _baseURL= MutableLiveData("http://10.0.2.2:8000/graphql/authenticate")
    val baseURL: LiveData<String> = _baseURL

    fun updateEndpoint(endpoint: String) {
        _baseURL.value = endpoint
    }
}