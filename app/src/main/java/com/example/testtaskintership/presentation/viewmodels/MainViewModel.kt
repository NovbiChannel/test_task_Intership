package com.example.testtaskintership.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.testtaskintership.data.api.ApiService
import com.example.testtaskintership.data.local.model.CameraModelRealm
import com.example.testtaskintership.data.local.model.DoorsModelRealm
import com.example.testtaskintership.data.local.repositories.RealmRepository
import com.example.testtaskintership.domain.model.Camera
import com.example.testtaskintership.domain.model.Data
import com.example.testtaskintership.domain.model.DataX
import com.example.testtaskintership.domain.model.MainDataModel
import com.example.testtaskintership.domain.model.SecondaryDataModel
import com.example.testtaskintership.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val service = ApiService.create()
    private val realm = RealmRepository.create()

    private val _cameras = MutableStateFlow<Resource<MainDataModel>>(Resource.Loading())
    val cameras: LiveData<MainDataModel?> = _cameras.asSharedFlow().map { it.data }.asLiveData()

    private val _doors = MutableStateFlow<Resource<SecondaryDataModel>>(Resource.Loading())
    val doors: LiveData<SecondaryDataModel?> = _doors.asSharedFlow().map { it.data }.asLiveData()

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    init {
        loadData()
    }

    private fun loadData() {
        loadCameras()
        loadDoors()
    }

    fun onRefresh() {
        _isRefreshing.value = true
        refreshCameras()
        refreshDoors()
    }

    private fun loadCameras() {
        viewModelScope.launch(Dispatchers.IO) {
            _cameras.value = Resource.Loading()

            try {
                val camerasFromDb = realm.getCameras().first()
                if (camerasFromDb.isNotEmpty()) {
                    val dataModel = MainDataModel(Data(emptyList(), camerasFromDb.map { cameraModelRealmToCamera(it)}), true)
                    _cameras.value = Resource.Success(dataModel)
                } else {
                    refreshCameras()
                }
            } catch (e: Exception) {
                _cameras.value = Resource.Error("Error retrieving cameras: ${e.message}")
            }
        }
    }

    private fun loadDoors() {
        viewModelScope.launch(Dispatchers.IO) {
            _doors.value = Resource.Loading()

            try {
                val doorsFromDb = realm.getDoors().first()
                if (doorsFromDb.isNotEmpty()) {
                    val dataModel = SecondaryDataModel(doorsFromDb.map { doorsModelRealmToDataX(it) }, true)
                    _doors.value = Resource.Success(dataModel)
                } else {
                    refreshDoors()
                }
            } catch (e: Exception) {
                _doors.value = Resource.Error("Error retrieving doors: ${e.message}")
            }
        }
    }

    private fun refreshCameras() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = service.getCameras()
                if (response.success) {
                    realm.deleteAllCameras()
                    response.data.cameras.forEach { camera ->
                        realm.insertCamera(cameraToCameraModelRealm(camera))
                    }
                    _cameras.value = Resource.Success(response)
                } else {
                    _cameras.value = Resource.Error("Error retrieving cameras")
                }
            } catch (e: Exception) {
                _cameras.value = Resource.Error("Error retrieving cameras: ${e.message}")
            } finally {
                _isRefreshing.postValue(false)
            }
        }
    }

    private fun refreshDoors() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = service.getDoors()
                if (response.success) {
                    realm.deleteAllDoors()
                    response.data.forEach { door ->
                        realm.insertDoor(doorsToDoorsModelRealm(door))
                    }
                    _doors.value = Resource.Success(response)
                } else {
                    _doors.value = Resource.Error("Error retrieving doors")
                }
            } catch (e: Exception) {
                _doors.value = Resource.Error("Error retrieving doors: ${e.message}")
            } finally {
                _isRefreshing.postValue(false)
            }
        }
    }

    private fun cameraToCameraModelRealm(camera: Camera): CameraModelRealm {
        return CameraModelRealm().apply {
            name = camera.name
            rec = camera.rec
            favorites = camera.favorites
            room = camera.room ?: "no_room"
            snapshot = camera.snapshot
        }
    }

    private fun doorsToDoorsModelRealm(dataX: DataX): DoorsModelRealm {
        return DoorsModelRealm().apply {
            name = dataX.name
            favorites = dataX.favorites
            room = dataX.room ?: "no_room"
            snapshot = dataX.snapshot ?: "no_snap"
        }
    }
    private fun cameraModelRealmToCamera(cameraModelRealm: CameraModelRealm): Camera {
        return Camera(
            favorites = cameraModelRealm.favorites,
            id = -1,
            name = cameraModelRealm.name,
            rec = cameraModelRealm.rec,
            room = cameraModelRealm.room,
            snapshot = cameraModelRealm.snapshot
        )
    }

    private fun doorsModelRealmToDataX(doorsModelRealm: DoorsModelRealm): DataX {
        return DataX(
            favorites = doorsModelRealm.favorites,
            id = -1,
            name = doorsModelRealm.name,
            room = doorsModelRealm.room,
            snapshot = doorsModelRealm.snapshot
        )
    }
}