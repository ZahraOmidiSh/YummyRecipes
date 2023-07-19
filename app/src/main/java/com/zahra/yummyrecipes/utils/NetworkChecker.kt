package com.zahra.yummyrecipes.utils

import android.net.ConnectivityManager
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class NetworkChecker @Inject constructor(
    private val manager: ConnectivityManager,
    private val request: NetworkRequest
) : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(): MutableStateFlow<Boolean> {
        //Register
        manager.registerNetworkCallback(request, this)
    }
}