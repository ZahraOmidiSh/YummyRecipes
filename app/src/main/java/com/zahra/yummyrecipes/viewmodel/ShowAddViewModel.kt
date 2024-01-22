package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ShowAddViewModel @Inject constructor() : ViewModel() {

    private val _showAddFlag = MutableLiveData<Int>()

    val showAddFlag: LiveData<Int> get() = _showAddFlag

    fun setShowAddFlag(value: Int) {
        _showAddFlag.value = value
    }
}