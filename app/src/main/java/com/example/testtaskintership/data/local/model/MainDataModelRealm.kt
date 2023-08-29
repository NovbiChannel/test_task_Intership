package com.example.testtaskintership.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CameraModelRealm: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var name: String = ""
    var rec: Boolean = false
    var favorites: Boolean = false
    var room: String = ""
    var snapshot: String = ""
}
class DoorsModelRealm: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var name: String = ""
    var favorites: Boolean = false
    var room: String = ""
    var snapshot: String = ""
}