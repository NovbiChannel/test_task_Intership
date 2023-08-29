package com.example.testtaskintership.data.local.repositories

import com.example.testtaskintership.data.local.model.CameraModelRealm
import com.example.testtaskintership.data.local.model.DoorsModelRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.flow.Flow

interface RealmRepository {
    fun getCameras(): Flow<List<CameraModelRealm>>
    fun getDoors(): Flow<List<DoorsModelRealm>>
    suspend fun insertCamera(camera: CameraModelRealm)
    suspend fun insertDoor(door: DoorsModelRealm)
    suspend fun updateCamera(camera: CameraModelRealm)
    suspend fun updateDoor(door: DoorsModelRealm)
    suspend fun hasCameras(): Boolean
    suspend fun hasDoors(): Boolean

    suspend fun deleteAllCameras()
    suspend fun deleteAllDoors()

    companion object {
        fun create(): RealmRepository {
            val config = RealmConfiguration.Builder(
                schema = setOf(
                    CameraModelRealm::class, DoorsModelRealm::class
                )
            )
                .compactOnLaunch()
                .build()
            return RealmRepositoryImpl (
                realm = Realm.open(config)
            )
        }
    }
}