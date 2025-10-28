package com.ceac.mvvmapp.domain.repository

import com.ceac.mvvmapp.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}
