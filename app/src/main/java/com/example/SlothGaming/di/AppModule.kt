package com.example.SlothGaming.di

import android.content.Context
import com.example.SlothGaming.data.local_db.ReviewListDao
import com.example.SlothGaming.data.local_db.ReviewListDataBase
import com.example.SlothGaming.data.retrofit.IgdbProxyApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideRetrofit() : Retrofit{
        return Retrofit.Builder().baseUrl("https://slothgamingapi.onrender.com").addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideIgdbApi(retrofit: Retrofit): IgdbProxyApi {
        return retrofit.create(IgdbProxyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ReviewListDataBase {
        //getDatabase function from ReviewListDataBase impl
        return ReviewListDataBase.getDatabase(context)
    }

    @Provides
    fun provideReviewDao(database: ReviewListDataBase): ReviewListDao {
        return database.reviewDao()
    }

    // Firebase

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}