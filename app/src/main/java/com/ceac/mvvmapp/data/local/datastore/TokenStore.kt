package com.ceac.mvvmapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Paso 3: Persistencia segura de tokens con DataStore.
 *
 * Explicación:
 * Esta clase gestiona el almacenamiento local de los tokens de acceso y refresco
 * usando **DataStore (Preferences)**, una alternativa moderna y segura a SharedPreferences.
 *
 * DataStore permite operaciones reactivas con `Flow`, evitando bloqueos de hilo y
 * garantizando consistencia en lecturas/escrituras concurrentes.
 *
 * Responsabilidades principales:
 * - Guardar los tokens recibidos del backend tras el login.
 * - Exponerlos como `Flow` para observar cambios en tiempo real.
 * - Ofrecer métodos `suspend` para obtenerlos o limpiarlos de forma puntual.
 *
 * Paso siguiente:
 * Se inyectará un `TokenStore` en los interceptores de red
 * (`AuthInterceptor`, `TokenAuthenticator`) para añadir los tokens en las cabeceras
 * o refrescarlos automáticamente cuando expiren.
 */
class TokenStore(
    /** Instancia de DataStore<Preferences> inyectada desde Hilt. */
    private val ds: DataStore<Preferences>
) {

    // -------------------------------------------------------------------------
    // Claves internas para los tokens
    // -------------------------------------------------------------------------

    /** Clave de almacenamiento para el access token (token de sesión). */
    private val KEY_ACCESS = stringPreferencesKey("access_token")

    /** Clave de almacenamiento para el refresh token (token de renovación). */
    private val KEY_REFRESH = stringPreferencesKey("refresh_token")

    // -------------------------------------------------------------------------
    // Flujos reactivos (lectura continua)
    // -------------------------------------------------------------------------

    /**
     * Flujo reactivo que emite el token de acceso cada vez que cambia.
     *
     * Útil para escenarios donde la UI o un interceptor quiera reaccionar
     * automáticamente cuando el usuario inicia o cierra sesión.
     */
    val accessTokenFlow: Flow<String?> = ds.data.map { it[KEY_ACCESS] }

    /**
     * Flujo reactivo que emite el refresh token cuando se actualiza.
     *
     * Normalmente solo lo usa el TokenAuthenticator para renovar tokens expirados.
     */
    val refreshTokenFlow: Flow<String?> = ds.data.map { it[KEY_REFRESH] }

    // -------------------------------------------------------------------------
    // Operaciones principales (escritura/lectura)
    // -------------------------------------------------------------------------

    /**
     * Guarda los tokens de sesión y de refresco en DataStore.
     *
     * @param access Token de acceso (Bearer).
     * @param refresh Token de refresco emitido por el backend.
     *
     * Ejemplo de uso:
     * ```
     * tokenStore.saveTokens(loginResponse.accessToken, loginResponse.refreshToken)
     * ```
     */
    suspend fun saveTokens(access: String, refresh: String) {
        ds.edit {
            it[KEY_ACCESS] = access
            it[KEY_REFRESH] = refresh
        }
    }

    /**
     * Elimina todos los tokens almacenados (logout o sesión inválida).
     *
     * Ejemplo de uso:
     * ```
     * tokenStore.clear()
     * ```
     */
    suspend fun clear() {
        ds.edit {
            it.remove(KEY_ACCESS)
            it.remove(KEY_REFRESH)
        }
    }

    // -------------------------------------------------------------------------
    // Lecturas puntuales (una sola vez)
    // -------------------------------------------------------------------------

    /**
     * Obtiene el token de acceso actual de forma suspendida.
     *
     * @return Token de acceso actual o `null` si no existe.
     */
    suspend fun accessToken(): String? = ds.data.first()[KEY_ACCESS]

    /**
     * Obtiene el token de refresco actual de forma suspendida.
     *
     * @return Token de refresco actual o `null` si no existe.
     */
    suspend fun refreshToken(): String? = ds.data.first()[KEY_REFRESH]
}
