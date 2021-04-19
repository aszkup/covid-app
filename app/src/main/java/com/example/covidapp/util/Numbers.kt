package com.example.covidapp.util

class Numbers {

    fun isPrimeNo(number: Int): Boolean {
        if (number < 2) return false
        for (i in 2..number / 2) {
            if (number % i == 0) {
                return false
            }
        }
        return true
    }
}
