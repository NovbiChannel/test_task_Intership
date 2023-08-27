package com.example.testtaskintership.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DataX(
    val favorites: Boolean,
    val id: Int,
    val name: String,
    val room: String? = "no_room",
    val snapshot: String? = "no_snap"
)