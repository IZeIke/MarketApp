package com.example.harit.marketapp.ui.di

import com.example.harit.marketapp.ui.loginPage.LoginActivity
import com.example.harit.marketapp.ui.loginPage.SignupActivity
import com.example.harit.marketapp.ui.SplashScreenActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [AppModule::class, FirebaseModule::class]
)

interface AppComponent {

    fun inject(loginActivity: LoginActivity)
    fun inject(splashScreenActivity: SplashScreenActivity)
    fun inject(signupActivity: SignupActivity)
}