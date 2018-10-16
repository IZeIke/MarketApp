package com.example.harit.marketapp.ui.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule{

    @Provides
    @Singleton
    fun provideFirebaseUser(mAuth : FirebaseAuth) : FirebaseUser?{
        return mAuth.currentUser
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

}