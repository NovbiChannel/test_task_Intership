package com.example.testtaskintership.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SecondaryDataModel(
    val `data`: List<DataX>,
    val success: Boolean
)