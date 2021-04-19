package com.example.covidapp.util

class Colors {

    fun getIntFromColor(red: Int, green: Int, blue: Int): Int {
        var red = red
        var green = green
        var blue = blue
        red = red shl 16 and 0x00FF0000 //Shift red 16-bits and mask out other stuff
        green = green shl 8 and 0x0000FF00 //Shift Green 8-bits and mask out other stuff
        blue = blue and 0x000000FF //Mask out anything not blue.
        return -0x1000000 or red or green or blue //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}
