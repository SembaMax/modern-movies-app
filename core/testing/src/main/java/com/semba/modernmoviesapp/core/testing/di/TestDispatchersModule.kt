package com.semba.modernmoviesapp.core.testing.di

import com.semba.modernmoviesapp.core.common.AppDispatcher
import com.semba.modernmoviesapp.core.common.Dispatcher
import com.semba.modernmoviesapp.core.common.di.DispatchersModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class]
)
object TestDispatchersModule {

    @Provides
    @Dispatcher(AppDispatcher.IO)
    fun provideTestIODispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
}