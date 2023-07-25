package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.TimerTask

class TimeViewModel : ViewModel() {
    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String>
        get() = _currentTime

    init {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val sdf = SimpleDateFormat("HH")
                val currentDate = sdf.format(Date())
                _currentTime.postValue(currentDate)
            }
        }, 0, 1000)
    }
}