package com.example.covidapp.view.util

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:text")
fun setText(textView: TextView, value: Int) {
    textView.text = value.toString()
}
