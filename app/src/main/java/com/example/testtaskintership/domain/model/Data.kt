package com.example.testtaskintership.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val room: List<String>,
    val cameras: List<Camera>
)