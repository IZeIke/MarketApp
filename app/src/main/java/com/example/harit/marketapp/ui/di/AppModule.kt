package com.example.harit.marketapp.ui.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideApplication() = application

    @Singleton
    @Provides
    fun provideSharedPreferences() = application.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
}