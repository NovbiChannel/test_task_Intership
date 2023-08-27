package com.example.testtaskintership.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Camera(
    val favorites: Boolean,
    val id: Int,
    val name: String,
    val rec: Boolean,
    val room: String? = "no_room",
    val snapshot: String
)