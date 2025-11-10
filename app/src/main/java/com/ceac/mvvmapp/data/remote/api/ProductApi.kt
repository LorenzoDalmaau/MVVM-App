package com.ceac.mvvmapp.data.remote.api

import com.ceac.mvvmapp.data.remote.dto.ProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {
    @GET("api/v1/products")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<List<ProductDto>>
}
