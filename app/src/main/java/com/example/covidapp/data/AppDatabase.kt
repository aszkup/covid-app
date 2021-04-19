package com.example.covidapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.covidapp.data.cases.Cases
import com.example.covidapp.data.cases.CasesDao
import com.example.covidapp.data.city.City
import com.example.covidapp.data.city.CityDao
import org.threeten.bp.Instant


const val DATABASE_NAME = "database"

@Database(
    version = 1,
    entities = [City::class, Cases::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCityDao(): CityDao
    abstract fun getCasesDao(): CasesDao
}

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }
}
