package com.example.covidapp.data.cases

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant

@Entity(tableName = CASES_TABLE_NAME)
data class Cases(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityId: Int,
    val number: Int,
    val date: Instant
)
