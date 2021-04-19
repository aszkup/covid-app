package com.example.covidapp.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.covidapp.R
import com.example.covidapp.databinding.ActivityMainBinding
import com.example.covidapp.view.util.EventLiveData
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named


class MainActivity : AppCompatActivity() {

    private val mainUiEvents: EventLiveData<MainUiEvents> by inject(qualifier = named(MAIN_UI_EVENTS))
    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        activityMainBinding.fab.setOnClickListener {
            mainUiEvents.postAsEvent(MainUiEvents.OnFabClicked)
        }
    }
}
