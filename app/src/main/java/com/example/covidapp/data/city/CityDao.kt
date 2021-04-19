package com.example.covidapp.data.city

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

const val CITY_TABLE_NAME = "city"

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: City)

    @Query("UPDATE $CITY_TABLE_NAME SET totalCases = :totalCases WHERE id = :cityId")
    fun update(cityId: Int, totalCases: Int)

    @Query("DELETE FROM $CITY_TABLE_NAME WHERE id = :cityId")
    fun delete(cityId: Int)

    @Query("SELECT * FROM $CITY_TABLE_NAME")
    fun getAll(): Flow<List<City>>
}
