package com.valmiraguiar.wefit

import android.app.Application
import com.valmiraguiar.wefit.di.MainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeFitApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WeFitApp)
        }
        MainModule.load()
    }

}