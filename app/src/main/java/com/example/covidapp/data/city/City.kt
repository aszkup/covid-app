package com.example.covidapp.data.city

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CITY_TABLE_NAME)
data class City(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val totalCases: Int  // store redundant totalCases to avoid JOIN on CasesTable and improve performance on DB
)
