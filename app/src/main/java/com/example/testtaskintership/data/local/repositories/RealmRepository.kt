package com.example.testtaskintership.data.local.repositories

import com.example.testtaskintership.data.local.model.CameraModelRealm
import com.example.testtaskintership.data.local.model.DoorsModelRealm
import com.example.testtaskintership.data.local.model.RoomModelRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.flow.Flow

interface RealmRepository {
    fun getCameras(): Flow<List<CameraModelRealm>>
    fun getDoors(): Flow<List<DoorsModelRealm>>
    fun getRooms(): Flow<List<RoomModelRealm>>
    suspend fun insertCamera(camera: CameraModelRealm)
    suspend fun insertDoor(door: DoorsModelRealm)
    suspend fun insertRoom(room: RoomModelRealm)
    suspend fun updateCamera(camera: CameraModelRealm, newFav: Boolean)
    suspend fun updateDoor(door: DoorsModelRealm, newName: String)
    suspend fun deleteAllCameras()
    suspend fun deleteAllDoors()
    suspend fun deleteAllRoom()

    companion object {
        fun create(): RealmRepository {
            val config = RealmConfiguration.Builder(
                schema = setOf(
                    CameraModelRealm::class, DoorsModelRealm::class, RoomModelRealm::class
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