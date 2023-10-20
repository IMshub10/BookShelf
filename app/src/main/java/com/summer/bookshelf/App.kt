package com.summer.bookshelf

import android.app.Application
import com.summer.bookshelf.di.remoteModule
import com.summer.bookshelf.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                remoteModule,
                viewModelModule
            )
        }
    }
}