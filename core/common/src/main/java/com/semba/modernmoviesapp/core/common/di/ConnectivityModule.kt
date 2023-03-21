package com.semba.modernmoviesapp.core.common.di

import com.semba.modernmoviesapp.core.common.connectivity.ConnectivityManagerNetworkMonitor
import com.semba.modernmoviesapp.core.common.connectivity.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ConnectivityModule {

    @Binds
    fun bindsConnectivityManager(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor
}