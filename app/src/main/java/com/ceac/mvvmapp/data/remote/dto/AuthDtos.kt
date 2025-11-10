package com.ceac.mvvmapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Paso 4.1: DTO de petición de login.
 *
 * Explicación:
 * Clase utilizada como cuerpo (@Body) en la llamada de autenticación.
 * Usa Moshi codegen para serializar a JSON sin reflexión.
 *
 * Recomendaciones:
 * - Si el backend usa nombres diferentes, añade @Json(name = "backend_field").
 * - Valida en dominio (UseCase) y mantén aquí solo el contrato de transporte.
 */
@JsonClass(generateAdapter = true)
data class LoginRequest(
    /** Correo electrónico del usuario. */
    @Json(name = "email") val email: String,
    /** Contraseña en texto plano; debe enviarse por HTTPS. */
    @Json(name = "password") val password: String
)

/**
 * Paso 4.2: DTO de respuesta de login.
 *
 * Explicación:
 * Respuesta con tokens de acceso y refresco. El adapter se genera
 * en compilación gracias a Moshi codegen (kapt).
 *
 * Consideraciones:
 * - Si el backend incluye vencimiento (exp) u otros campos, añádelos aquí.
 * - Estos tokens no deben guardarse en claro fuera de DataStore (preferible cifrar).
 */
@JsonClass(generateAdapter = true)
data class LoginResponse(
    /** Token de acceso breve (se adjunta en Authorization: Bearer …). */
    @Json(name = "accessToken") val accessToken: String,
    /** Token de refresco de mayor duración. */
    @Json(name = "refreshToken") val refreshToken: String
)

/**
 * Paso 4.3: DTO de petición de refresh.
 *
 * Explicación:
 * Envío del refresh token para obtener un nuevo access token cuando
 * el servidor responde con 401.
 */
@JsonClass(generateAdapter = true)
data class RefreshRequest(
    /** Token de refresco previamente emitido por el backend. */
    @Json(name = "refreshToken") val refreshToken: String
)

/**
 * Paso 4.4: DTO de respuesta de refresh.
 *
 * Explicación:
 * Respuesta con un nuevo access token. El refresh token puede permanecer
 * igual o rotarse según política del backend.
 */
@JsonClass(generateAdapter = true)
data class RefreshResponse(
    /** Nuevo token de acceso válido. */
    @Json(name = "accessToken") val accessToken: String
)

/**
 * Paso siguiente:
 * - Implementar la interfaz de Retrofit (AuthApi) con:
 *   @POST("api/v1/auth/login") suspend fun login(@Body req: LoginRequest): Response<LoginResponse>
 *   @POST("api/v1/auth/refresh") suspend fun refresh(@Body req: RefreshRequest): Response<RefreshResponse>
 * - Conectar estos DTOs en AuthRepositoryImpl usando safeApiCall.
 */
