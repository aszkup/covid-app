package com.example.covidapp.data.city

import com.example.covidapp.data.AppDatabase
import com.example.covidapp.view.cities.CitiesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val citiesModule = module {

    viewModel { CitiesViewModel(get(), get(), get(), get()) }
    single { CitiesRepository(get(), get()) }
    single<CityDao> { providesCityDao(get()) }
}

private fun providesCityDao(appDatabase: AppDatabase) = appDatabase.getCityDao()
