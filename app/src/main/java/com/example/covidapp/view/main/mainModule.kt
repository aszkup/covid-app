package com.example.covidapp.view.main

import com.example.covidapp.util.Colors
import com.example.covidapp.util.Numbers
import com.example.covidapp.view.util.EventLiveData
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    single { Numbers() }
    single { Colors() }
    single(qualifier = named(MAIN_UI_EVENTS)) { EventLiveData<MainUiEvents>() }
}

const val MAIN_UI_EVENTS = "mainUiEvents"
