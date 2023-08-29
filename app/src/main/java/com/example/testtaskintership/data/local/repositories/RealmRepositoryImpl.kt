package com.example.testtaskintership.data.local.repositories

import android.util.Log
import com.example.testtaskintership.data.local.model.CameraModelRealm
import com.example.testtaskintership.data.local.model.DoorsModelRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealmRepositoryImpl(val realm: Realm): RealmRepository {
    override fun getCameras(): Flow<List<CameraModelRealm>> {
        return realm.query<CameraModelRealm>().asFlow().map { it.list }
    }

    override fun getDoors(): Flow<List<DoorsModelRealm>> {
        return realm.query<DoorsModelRealm>().asFlow().map { it.list }
    }

    override suspend fun insertCamera(camera: CameraModelRealm) {
        realm.write { copyToRealm(camera) }
    }

    override suspend fun insertDoor(door: DoorsModelRealm) {
        realm.write { copyToRealm(door) }
    }

    override suspend fun updateCamera(camera: CameraModelRealm) {
        TODO("Not yet implemented")
    }

    override suspend fun updateDoor(door: DoorsModelRealm) {
        TODO("Not yet implemented")
    }

    override suspend fun hasCameras(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun hasDoors(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCameras() {
        realm.write {
            val cameras = query<CameraModelRealm>().find()
            try {
                cameras.forEach { camera ->
                    delete(camera)
                }
            } catch (e: Exception) {
                Log.e("RealmRepositoryImpl", e.message.toString())
            }
        }
    }

    override suspend fun deleteAllDoors() {
        realm.write {
            val doors = query<DoorsModelRealm>().find()
            try {
                doors.forEach { door ->
                    delete(door)
                }
            } catch (e: Exception) {
                Log.e("RealmRepositoryImpl", e.message.toString())
            }
        }
    }
}