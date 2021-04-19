package com.example.covidapp.data.cases

import androidx.room.Transaction
import com.example.covidapp.data.city.CityDao
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.Instant

class CasesRepository(
    private val casesDao: CasesDao,
    private val cityDao: CityDao
) {

    fun getItems(cityId: Int): Flow<List<Cases>> = casesDao.getAllWith(cityId)

    fun getTotalCases(cityId: Int): Flow<Int> = casesDao.getCasesCount(cityId)

    @Transaction
    fun add(cases: Cases, totalCases: Int) {
        casesDao.insert(cases)
        cityDao.update(cases.cityId, totalCases)
    }

    fun remove(cityId: Int, date: Instant) {
        casesDao.deleteWith(cityId, date)
    }

    fun removeAll() {
        casesDao.deleteAll()
    }
}
