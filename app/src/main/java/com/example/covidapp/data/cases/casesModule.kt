package com.example.covidapp.data.cases

import com.example.covidapp.data.AppDatabase
import com.example.covidapp.view.cases.NewCasesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val casesModule = module {
    viewModel { parameters ->
        NewCasesViewModel(
            cityId = parameters.get(), cityName = parameters.get(), casesRepository = get()
        )
    }
    single<CasesDao> { providesCasesDao(get()) }
    single { CasesRepository(get(), get()) }
}

private fun providesCasesDao(appDatabase: AppDatabase) = appDatabase.getCasesDao()
