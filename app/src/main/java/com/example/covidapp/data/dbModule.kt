package com.example.covidapp.data

import android.content.Context
import androidx.room.Room
import org.koin.dsl.module

val dbModule = module {

    single<AppDatabase> { getAppDatabase(get()) }

}

private fun getAppDatabase(context: Context) = Room.databaseBuilder<AppDatabase>(
    context.applicationContext, AppDatabase::class.java, DATABASE_NAME
).fallbackToDestructiveMigration().build()
