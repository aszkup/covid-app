package com.example.covidapp

import android.app.Application
import com.example.covidapp.data.cases.casesModule
import com.example.covidapp.data.city.citiesModule
import com.example.covidapp.data.dbModule
import com.example.covidapp.view.main.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CovidApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@CovidApp)
            modules(modules)
        }
    }
}

private val modules = listOf(
    dbModule,
    mainModule,
    citiesModule,
    casesModule
)
