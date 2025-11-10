package com.ceac.mvvmapp.data.remote.interceptor

import com.ceac.mvvmapp.data.local.datastore.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Paso 6: Implementación del interceptor de autenticación (`AuthInterceptor`).
 *
 * Explicación:
 * Este interceptor se ejecuta **antes de cada petición HTTP** realizada por Retrofit,
 * y su propósito es añadir el encabezado `Authorization` con el token JWT del usuario.
 *
 * De este modo, las peticiones autenticadas (por ejemplo `/products`, `/profile`, etc.)
 * incluyen automáticamente el token guardado en `DataStore`, sin necesidad de
 * modificar manualmente cada llamada.
 *
 * Arquitectura:
 * - Pertenece a la capa **data → remote → interceptor**.
 * - Se usa en el cliente OkHttp configurado dentro de `AppModule`.
 * - Trabaja junto a `TokenAuthenticator`, que se encarga de renovar el token si expira.
 *
 * Flujo de ejecución:
 * 1. Cada vez que Retrofit va a enviar una petición, pasa por este interceptor.
 * 2. Se obtiene el token actual desde `TokenStore` (almacenado en DataStore).
 * 3. Si existe, se añade en el header HTTP → `Authorization: Bearer <token>`.
 * 4. La petición se reenvía (`chain.proceed(req)`).
 *
 * Nota:
 * - `runBlocking` se usa porque los interceptores de OkHttp no admiten funciones suspendidas.
 *   Solo se utiliza aquí porque la operación de lectura es rápida (en memoria / DataStore).
 *
 * Paso siguiente:
 * Implementar el `TokenAuthenticator` para manejar la renovación automática
 * del token cuando el backend devuelva un código `401 Unauthorized`.
 */
class AuthInterceptor(
    /** Instancia de `TokenStore` usada para recuperar el token de acceso. */
    private val tokenStore: TokenStore
) : Interceptor {

    /**
     * Intercepta la petición HTTP y añade el header de autenticación si es necesario.
     *
     * @param chain Cadena de interceptores de OkHttp.
     * @return La respuesta del servidor tras aplicar el header (si procede).
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        // Petición original sin modificar
        val orig = chain.request()

        // Recuperamos el token de acceso de DataStore de forma bloqueante
        val access = runBlocking { tokenStore.accessToken() }

        // Si existe token, lo añadimos al header "Authorization"
        val req = if (!access.isNullOrBlank()) {
            orig.newBuilder()
                .addHeader("Authorization", "Bearer $access")
                .build()
        } else orig

        // Continuamos con la ejecución de la petición
        return chain.proceed(req)
    }
}
