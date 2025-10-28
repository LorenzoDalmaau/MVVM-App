package com.ceac.mvvmapp.domain.model

/// TODO Añadir Comentario
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)
