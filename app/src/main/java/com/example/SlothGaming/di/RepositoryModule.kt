package com.example.SlothGaming.di

import com.example.SlothGaming.data.fire_base.FirebaseAuthImpl
import com.example.SlothGaming.data.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        auth: FirebaseAuthImpl
    ): AuthRepository
}