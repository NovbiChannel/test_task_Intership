package com.example.testtaskintership.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MainDataModel(
    val `data`: Data,
    val success: Boolean
)