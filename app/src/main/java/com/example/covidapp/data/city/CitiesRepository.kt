package com.example.covidapp.data.city

import androidx.room.Transaction
import com.example.covidapp.data.cases.CasesDao
import kotlinx.coroutines.flow.Flow

class CitiesRepository(
    private val cityDao: CityDao,
    private val casesDao: CasesDao
) {

    val items: Flow<List<City>> = cityDao.getAll()

    fun add(city: City) {
        cityDao.insert(city)
    }

    @Transaction
    fun remove(cityId: Int) {
        cityDao.delete(cityId)
        casesDao.deleteWith(cityId)
    }

    fun getRandomCity() = cities.shuffled().first()
}

private val cities = listOf<String>(
    "London", "New York", "Paris", "San Francisco", "Berlin", "Las Vegas", "Hague", "Madrid", "Oslo", "Monaco",
    "Warsaw", "Sofia", "Sydney", "Manchester"
)
