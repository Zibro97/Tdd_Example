package com.zibro.fooddeliveryapp

import android.app.Application
import com.zibro.fooddeliveryapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    companion object{
        var instance : App? = null
        private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }
}