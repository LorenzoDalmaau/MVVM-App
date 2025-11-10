package com.ceac.mvvmapp.data.repository

import com.ceac.mvvmapp.data.local.datastore.TokenStore
import com.ceac.mvvmapp.data.remote.api.auth.AuthApi
import com.ceac.mvvmapp.data.remote.dto.LoginRequest
import com.ceac.mvvmapp.data.util.NetworkResult
import com.ceac.mvvmapp.data.util.safeApiCall
import com.ceac.mvvmapp.domain.repository.auth.AuthRepository
import java.io.IOException
import javax.inject.Inject

/**
 * Paso 7: Implementación del repositorio de autenticación.
 *
 * Explicación:
 * Esta clase es la encargada de coordinar la comunicación entre:
 * - El servicio remoto de autenticación (`AuthApi`) y
 * - El almacenamiento local de tokens (`TokenStore`).
 *
 * Representa la capa de **datos** dentro de la arquitectura en capas (Clean Architecture),
 * traduciendo los resultados de la red a un formato entendible por el dominio.
 *
 * Los métodos del repositorio devuelven un `Result<Unit>` para simplificar la gestión
 * de éxito/fracaso desde los casos de uso (UseCases) o ViewModels.
 *
 * Uso interno:
 * - `safeApiCall { ... }` encapsula la llamada Retrofit y devuelve un `NetworkResult`
 *   que distingue entre éxito, error HTTP, error de red o excepción desconocida.
 *
 * Paso siguiente:
 * Implementar los métodos pendientes (`recoverPassword`, `register`) y manejar
 * sus DTOs correspondientes cuando el backend esté disponible.
 */
class AuthRepositoryImpl @Inject constructor(
    /** API de autenticación inyectada por Hilt. */
    private val api: AuthApi,
    /** Almacén de tokens local (DataStore). */
    private val tokens: TokenStore
) : AuthRepository {

    // -------------------------------------------------------------------------
    // Login
    // -------------------------------------------------------------------------

    /**
     * Realiza el proceso de autenticación de usuario.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña en texto plano (se enviará cifrada por HTTPS).
     * @return `Result.success(Unit)` si el login fue correcto, o `Result.failure(Exception)` en caso de error.
     *
     * Flujo de ejecución:
     * 1. Se envía una petición `POST /auth/login` con el cuerpo [LoginRequest].
     * 2. Se procesa la respuesta mediante `safeApiCall`.
     * 3. Si es exitosa, se guardan los tokens en `TokenStore`.
     * 4. En caso de error, se devuelve un `Result.failure` con una excepción contextual.
     */
    override suspend fun login(email: String, password: String): Result<Unit> {
        // safeApiCall encapsula try/catch + comprobación de código HTTP
        return when (val res = safeApiCall { api.login(LoginRequest(email, password)) }) {
            is NetworkResult.Success -> {
                // Guardamos los tokens de sesión de forma persistente
                tokens.saveTokens(res.data.accessToken, res.data.refreshToken)
                Result.success(Unit)
            }
            is NetworkResult.ApiError -> {
                // Error HTTP controlado (por ejemplo 400 o 401)
                Result.failure(Exception("API ${res.code} - ${res.message}"))
            }
            is NetworkResult.NetworkError -> {
                // Error de conexión, DNS, timeout, etc.
                Result.failure(IOException("Network error"))
            }
            is NetworkResult.UnknownError -> {
                // Cualquier excepción inesperada (parseo, null, etc.)
                Result.failure(res.throwable)
            }
        }
    }

    // -------------------------------------------------------------------------
    // Recuperar contraseña (a implementar)
    // -------------------------------------------------------------------------

    /**
     * Enviará una solicitud de recuperación de contraseña al backend.
     *
     * @param email Correo electrónico del usuario.
     * @return Resultado del proceso (éxito o fallo).
     *
     * TODO: Implementar cuando el endpoint esté disponible en el backend.
     */
    override suspend fun recoverPassword(email: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    // -------------------------------------------------------------------------
    // Registro de usuario (a implementar)
    // -------------------------------------------------------------------------

    /**
     * Enviará los datos de registro de un nuevo usuario.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña elegida por el usuario.
     * @return Resultado del registro.
     *
     * TODO: Implementar cuando el endpoint esté disponible en el backend.
     */
    override suspend fun register(email: String, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    // -------------------------------------------------------------------------
    // Logout
    // -------------------------------------------------------------------------

    /**
     * Elimina los tokens almacenados localmente.
     *
     * Este método no realiza ninguna llamada al backend (logout local),
     * simplemente borra los tokens del DataStore para invalidar la sesión.
     *
     * Ejemplo de uso:
     * ```
     * authRepository.logout()
     * ```
     */
    override suspend fun logout() {
        tokens.clear()
    }
}
