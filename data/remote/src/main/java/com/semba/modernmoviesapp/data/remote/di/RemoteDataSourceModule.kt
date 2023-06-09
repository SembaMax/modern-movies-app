package com.semba.modernmoviesapp.data.remote.di

import com.semba.modernmoviesapp.data.remote.datasource.NetworkDataSource
import com.semba.modernmoviesapp.data.remote.datasource.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindsLocalDataSource(dataSource: NetworkDataSource): RemoteDataSource
}