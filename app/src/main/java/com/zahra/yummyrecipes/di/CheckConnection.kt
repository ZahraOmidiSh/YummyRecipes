package com.zahra.yummyrecipes.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CheckConnection {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideNetworkRequest() :NetworkRequest = NetworkRequest.Builder().apply {
        addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        //Android M
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        //Android P
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            addCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)
    }.build()

}