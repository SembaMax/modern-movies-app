package com.semba.modernmoviesapp.core.testing.di

import com.semba.modernmoviesapp.core.testing.TestMoviesRepository
import com.semba.modernmoviesapp.data.repository.Repository
import com.semba.modernmoviesapp.data.repository.di.RepositoryModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {

    @Binds
    abstract fun bindTestRepository(repository: TestMoviesRepository): Repository
}