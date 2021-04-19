package com.example.covidapp.data.cases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.Instant

const val CASES_TABLE_NAME = "cases"

@Dao
interface CasesDao {

    @Query("SELECT * FROM $CASES_TABLE_NAME")
    fun getAll(): Flow<List<Cases>>

    @Query("SELECT id, cityId, CAST(SUM(number) as INT) as number, date FROM $CASES_TABLE_NAME WHERE cityId = :cityId GROUP BY date ORDER BY date DESC ")
    fun getAllWith(cityId: Int): Flow<List<Cases>>

    @Query("SELECT SUM(number) FROM $CASES_TABLE_NAME WHERE cityId = :cityId")
    fun getCasesCount(cityId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cases: Cases)

    @Query("DELETE FROM $CASES_TABLE_NAME WHERE cityId = :cityId")
    fun deleteWith(cityId: Int)

    @Query("DELETE FROM $CASES_TABLE_NAME WHERE cityId = :cityId AND date = :date")
    fun deleteWith(cityId: Int, date: Instant)

    @Query("DELETE FROM $CASES_TABLE_NAME")
    fun deleteAll()
}
