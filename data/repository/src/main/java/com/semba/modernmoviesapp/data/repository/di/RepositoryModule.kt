package com.semba.modernmoviesapp.data.repository.di

import com.semba.modernmoviesapp.data.repository.MoviesRepository
import com.semba.modernmoviesapp.data.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsSearchRepository(repository: MoviesRepository): Repository
}