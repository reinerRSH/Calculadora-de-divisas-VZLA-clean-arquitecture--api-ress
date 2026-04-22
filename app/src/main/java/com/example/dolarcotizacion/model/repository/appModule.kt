package com.example.dolarcotizacion.model.repository

import android.content.Context
import androidx.room.Room
import com.example.dolarcotizacion.model.data.appDatabase
import com.example.dolarcotizacion.model.network.ApiService
import com.example.dolarcotizacion.model.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object appModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {

        return Retrofit.Builder()
            .baseUrl("https://ve.dolarapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): appDatabase {

        return Room.databaseBuilder(
            context,
            appDatabase::class.java,
            "app_database"
        ).build()


    }

    @Provides
    fun provideDolarDao(database: appDatabase) = database.dolarDao()
}