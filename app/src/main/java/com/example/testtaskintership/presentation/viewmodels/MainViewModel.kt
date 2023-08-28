package com.example.testtaskintership.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.testtaskintership.data.api.ApiService
import com.example.testtaskintership.domain.model.Data
import com.example.testtaskintership.domain.model.MainDataModel
import com.example.testtaskintership.domain.model.SecondaryDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val service = ApiService.create()

    private val _cameras = MutableStateFlow(MainDataModel(Data(emptyList(), emptyList()), false))
    val cameras = _cameras.asSharedFlow().asLiveData()

    private val _doors = MutableStateFlow(SecondaryDataModel(emptyList(), false))
    val doors = _doors.asSharedFlow().asLiveData()

    init {
        getCameras()
        getDoors()
    }

    private fun getCameras() = viewModelScope.launch {
        service.getCameras().let {
            if (it.success) {
                _cameras.value = it
            } else {
                _cameras.value = MainDataModel(Data(emptyList(), emptyList()), false)
            }
        }
    }

    private fun getDoors() = viewModelScope.launch {
        service.getDoors().let {
            if (it.success) {
                _doors.value = it
            } else {
                _doors.value = SecondaryDataModel(emptyList(), false)
            }
        }
    }
}