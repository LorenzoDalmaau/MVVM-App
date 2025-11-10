package com.ceac.mvvmapp.data.remote.api.auth

import com.ceac.mvvmapp.data.remote.dto.LoginRequest
import com.ceac.mvvmapp.data.remote.dto.LoginResponse
import com.ceac.mvvmapp.data.remote.dto.RefreshRequest
import com.ceac.mvvmapp.data.remote.dto.RefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

    @POST("api/v1/auth/refresh")
    suspend fun refresh(@Body req: RefreshRequest): Response<RefreshResponse>
}