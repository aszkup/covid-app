package com.example.covidapp.view.cases

import org.threeten.bp.Instant

data class CasesUiModel(
    val id: Int,
    val number: Int,
    val date: Instant,
    val dateText: String
)
