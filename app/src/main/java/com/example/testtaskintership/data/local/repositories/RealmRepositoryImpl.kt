package com.example.testtaskintership.data.local.repositories

import android.util.Log
import com.example.testtaskintership.data.local.model.CameraModelRealm
import com.example.testtaskintership.data.local.model.DoorsModelRealm
import com.example.testtaskintership.data.local.model.RoomModelRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealmRepositoryImpl(val realm: Realm): RealmRepository {
    override fun getCameras(): Flow<List<CameraModelRealm>> {
        return realm.query<CameraModelRealm>().asFlow().map { it.list }
    }

    override fun getDoors(): Flow<List<DoorsModelRealm>> {
        return realm.query<DoorsModelRealm>().asFlow().map { it.list }
    }

    override fun getRooms(): Flow<List<RoomModelRealm>> {
        return realm.query<RoomModelRealm>().asFlow().map { it.list }
    }

    override suspend fun insertCamera(camera: CameraModelRealm) {
        realm.write { copyToRealm(camera) }
    }

    override suspend fun insertDoor(door: DoorsModelRealm) {
        realm.write { copyToRealm(door) }
    }

    override suspend fun insertRoom(room: RoomModelRealm) {
        realm.write { copyToRealm(room) }
    }

    override suspend fun updateCamera(camera: CameraModelRealm, newFav: Boolean) {
        realm.write {
            val queryCamera = query<CameraModelRealm>(query = "id == $0", camera.id).first().find()
            if (newFav != camera.favorites) queryCamera?.favorites = newFav
        }
    }

    override suspend fun updateDoor(door: DoorsModelRealm, newName: String) {
        realm.write {
            val queryDoor = query<DoorsModelRealm>(query = "id == $0", door.id).first().find()
            queryDoor?.name = newName
        }
    }

    override suspend fun deleteAllCameras() {
        deleteAllOfType<CameraModelRealm>()
    }

    override suspend fun deleteAllDoors() {
        deleteAllOfType<DoorsModelRealm>()
    }

    override suspend fun deleteAllRoom() {
        deleteAllOfType<RoomModelRealm>()
    }

    private suspend inline fun <reified T : RealmObject> deleteAllOfType() {
        realm.write {
            try {
                delete(T::class)
            } catch (e: Exception) {
                Log.e("RealmRepositoryImpl", e.message.toString())
            }
        }
    }
}