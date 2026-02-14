package com.example.SlothGaming.di

import android.content.Context
import com.example.SlothGaming.data.local_db.GameDao
import com.example.SlothGaming.data.local_db.ReviewListDao
import com.example.SlothGaming.data.local_db.SlothGamingDataBase
import com.example.SlothGaming.data.remote_db.IgdbService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson) : Retrofit{
        return Retrofit.Builder().baseUrl("https://slothgamingapi.onrender.com")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    @Provides
    @Singleton
    fun provideIgdbApi(retrofit: Retrofit): IgdbService {
        return retrofit.create(IgdbService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SlothGamingDataBase {
        //getDatabase function from SlothGamingDataBase impl
        return SlothGamingDataBase.getDatabase(context)
    }

    @Provides
    fun provideReviewDao(database: SlothGamingDataBase): ReviewListDao {
        return database.reviewDao()
    }

    // Firebase
    @Provides
    fun provideGameDao(database: SlothGamingDataBase) : GameDao{
        return database.gameDao()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}