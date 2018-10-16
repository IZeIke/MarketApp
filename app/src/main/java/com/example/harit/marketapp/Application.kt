package com.example.harit.marketapp

import android.app.Application
import com.example.harit.marketapp.ui.di.AppComponent
import com.example.harit.marketapp.ui.di.AppModule
import com.example.harit.marketapp.ui.di.DaggerAppComponent
import com.example.harit.marketapp.ui.di.FirebaseModule
import com.facebook.stetho.Stetho

class Application : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        appComponent = DaggerAppComponent.builder()
                        .appModule(AppModule(this))
                        .firebaseModule(FirebaseModule())
                        .build()
    }
}