package com.example.covidapp.view.cities

import com.example.covidapp.R

data class CityUiModel(
    val id: Int,
    val name: String,
    val totalCases: Int,
    val shouldBoldText: Boolean,
    val backgroundColor: Int
) {
    val casesTextStyle
        get() = if (shouldBoldText) R.style.CasesTextBold else R.style.TextAppearance_AppCompat
}
