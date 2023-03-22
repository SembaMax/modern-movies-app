package com.semba.modernmoviesapp.core.testing.di

import com.semba.modernmoviesapp.core.common.connectivity.NetworkMonitor
import com.semba.modernmoviesapp.core.common.di.ConnectivityModule
import com.semba.modernmoviesapp.core.testing.FakeNetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ConnectivityModule::class]
)
abstract class TestConnectivityModule {

    @Binds
    abstract fun bindsConnectivityManager(networkMonitor: FakeNetworkMonitor): NetworkMonitor
}