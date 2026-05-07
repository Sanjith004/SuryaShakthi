package com.example.suryashakthi.di

import android.content.Context
import androidx.room.Room
import com.example.suryashakthi.data.local.EnergyDao
import com.example.suryashakthi.data.local.EnergyDatabase
import com.example.suryashakthi.data.remote.GeminiApi
import com.example.suryashakthi.data.repository.EnergyRepositoryImpl
import com.example.suryashakthi.domain.repository.EnergyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindRepository(impl: EnergyRepositoryImpl): EnergyRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): EnergyDatabase {
            return Room.databaseBuilder(context, EnergyDatabase::class.java, "surya_shakti.db").build()
        }

        @Provides
        fun provideDao(db: EnergyDatabase): EnergyDao = db.dao()

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        @Provides
        @Singleton
        fun provideGeminiApi(retrofit: Retrofit): GeminiApi = retrofit.create(GeminiApi::class.java)
    }
}
