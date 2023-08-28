package com.example.testtaskintership.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.testtaskintership.data.api.ApiService
import com.example.testtaskintership.domain.model.MainDataModel
import com.example.testtaskintership.domain.model.SecondaryDataModel
import com.example.testtaskintership.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val service = ApiService.create()

    private val _cameras = MutableStateFlow<Resource<MainDataModel>>(Resource.Loading())
    val cameras: LiveData<MainDataModel?> = _cameras.asSharedFlow().map { it.data }.asLiveData()

    private val _doors = MutableStateFlow<Resource<SecondaryDataModel>>(Resource.Loading())
    val doors: LiveData<SecondaryDataModel?> = _doors.asSharedFlow().map { it.data }.asLiveData()

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    init {
        refreshData()
    }

    private fun refreshData() {
        refreshCameras()
        refreshDoors()
    }

    fun refreshCameras() {
        _isRefreshing.postValue(true)
        fetchCameras()
    }

    fun refreshDoors() {
        _isRefreshing.postValue(true)
        fetchDoors()
    }

    private fun fetchCameras() = viewModelScope.launch(Dispatchers.IO) {
        _cameras.value = Resource.Loading()
        try {
            service.getCameras().also { response ->
                if (response.success) {
                    _cameras.value = Resource.Success(response)
                } else {
                    _cameras.value = Resource.Error("Error retrieving cameras")
                }
            }
        } catch (e: Exception) {
            _cameras.value = Resource.Error("Error retrieving cameras: ${e.message}")
        } finally {
            _isRefreshing.postValue(false)
        }
    }

    private fun fetchDoors() = viewModelScope.launch(Dispatchers.IO) {
        _doors.value = Resource.Loading()
        try {
            service.getDoors().also { response ->
                if (response.success) {
                    _doors.value = Resource.Success(response)
                } else {
                    _doors.value = Resource.Error("Error retrieving doors")
                }
            }
        } catch (e: Exception) {
            _doors.value = Resource.Error("Error retrieving doors: ${e.message}")
        } finally {
            _isRefreshing.postValue(false)
        }
    }
}