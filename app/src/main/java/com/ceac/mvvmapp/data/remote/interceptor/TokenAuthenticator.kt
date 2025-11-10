package com.ceac.mvvmapp.data.remote.interceptor

import com.ceac.mvvmapp.data.local.datastore.TokenStore
import com.ceac.mvvmapp.data.remote.api.auth.AuthApi
import com.ceac.mvvmapp.data.remote.dto.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Paso 7: Implementación del autenticador de token (`TokenAuthenticator`).
 *
 * Explicación:
 * Este componente actúa como **segunda línea de defensa** en la comunicación autenticada.
 * Si el backend devuelve un error **401 Unauthorized**, OkHttp ejecuta automáticamente
 * este autenticador antes de notificar el fallo a Retrofit.
 *
 * Su función es **intentar renovar el token de acceso (`accessToken`)**
 * usando el `refreshToken` almacenado localmente, sin que el usuario tenga que iniciar sesión otra vez.
 *
 * Flujo completo:
 * 1. El servidor responde con `401 Unauthorized`.
 * 2. OkHttp llama al método `authenticate()`.
 * 3. El autenticador lee el `refreshToken` desde `TokenStore`.
 * 4. Hace una llamada a `authApi.refresh(...)` para obtener un nuevo `accessToken`.
 * 5. Si la renovación tiene éxito:
 *      - Guarda el nuevo token en `DataStore`.
 *      - Reintenta automáticamente la petición original con el nuevo header.
 * 6. Si falla:
 *      - Limpia los tokens → el usuario deberá volver a iniciar sesión manualmente.
 *
 * Arquitectura:
 * - Trabaja junto a `AuthInterceptor`: este añade el token actual.
 * - Si el token está caducado, este autenticador se encarga de renovarlo.
 * - Ambos se configuran en `AppModule` dentro del cliente `authedClient`.
 *
 * Paso siguiente:
 * Crear el `ProductRepositoryImpl` para realizar peticiones autenticadas
 * utilizando este sistema de interceptores y autenticadores.
 */
class TokenAuthenticator(
    /** Cliente Retrofit que gestiona las peticiones de autenticación. */
    private val authApi: AuthApi,

    /** Componente encargado de guardar y recuperar tokens desde DataStore. */
    private val tokenStore: TokenStore
) : Authenticator {

    /**
     * Método invocado automáticamente cuando el servidor devuelve un `401 Unauthorized`.
     *
     * @param route Ruta HTTP (no siempre disponible, puede ser null).
     * @param response Respuesta original fallida del servidor.
     * @return Nueva petición con token actualizado o `null` si la autenticación falla.
     */
    override fun authenticate(route: Route?, response: Response): Request? {
        // Evita bucles infinitos (por ejemplo, si el nuevo token también da 401)
        if (responseCount(response) >= 2) return null

        // Obtiene el refresh token actual desde DataStore
        val refresh = runBlocking { tokenStore.refreshToken() } ?: return null

        // Solicita un nuevo token al backend
        val refreshResp = runBlocking { authApi.refresh(RefreshRequest(refresh)) }

        // Si la respuesta es exitosa, actualiza y reintenta la petición
        if (refreshResp.isSuccessful) {
            val newAccess = refreshResp.body()?.accessToken ?: return null

            // Guarda los nuevos tokens en DataStore
            runBlocking { tokenStore.saveTokens(newAccess, refresh) }

            // Reintenta la petición original con el nuevo access token
            return response.request.newBuilder()
                .header("Authorization", "Bearer $newAccess")
                .build()
        }

        // Si la renovación falla, limpiamos los tokens y devolvemos null
        runBlocking { tokenStore.clear() }
        return null
    }

    /**
     * Calcula cuántas veces se ha intentado responder a la misma petición.
     * Se usa para prevenir bucles infinitos de autenticación.
     *
     * @param response La respuesta actual del servidor.
     * @return Número de reintentos acumulados.
     */
    private fun responseCount(response: Response): Int {
        var r: Response? = response
        var count = 1
        while (r?.priorResponse != null) {
            count++
            r = r.priorResponse
        }
        return count
    }
}
