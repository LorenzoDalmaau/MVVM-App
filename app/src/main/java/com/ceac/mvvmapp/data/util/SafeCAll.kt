package com.ceac.mvvmapp.data.util

import retrofit2.Response
import java.io.IOException

/**
 * Paso 4: Manejo centralizado de errores de red con `safeApiCall`.
 *
 * Explicación:
 * Este método de extensión encapsula las llamadas a la API (Retrofit) y
 * las traduce en un tipo seguro y controlado: [NetworkResult].
 *
 * El objetivo es **unificar la gestión de errores** en toda la aplicación:
 * - Evitar duplicar `try/catch` en cada repositorio.
 * - Diferenciar entre errores HTTP, de red y excepciones desconocidas.
 * - Garantizar que los repositorios reciban siempre un tipo consistente.
 *
 * Flujo de ejecución:
 * 1. Se ejecuta el bloque `block()` que contiene la llamada Retrofit.
 * 2. Si la respuesta es `isSuccessful`, se devuelve un `NetworkResult.Success`.
 * 3. Si hay error HTTP (por ejemplo, código 400 o 500), se devuelve `ApiError`.
 * 4. Si hay un error de red (sin conexión, timeout, DNS...), se devuelve `NetworkError`.
 * 5. Cualquier otra excepción (por ejemplo `NullPointerException`) produce `UnknownError`.
 *
 * Ejemplo de uso:
 * ```
 * val result = safeApiCall { productApi.getProducts(page, size) }
 * when (result) {
 *     is NetworkResult.Success -> println(result.data)
 *     is NetworkResult.ApiError -> println("API error ${result.code}")
 *     is NetworkResult.NetworkError -> println("No hay conexión")
 *     is NetworkResult.UnknownError -> println(result.throwable)
 * }
 * ```
 *
 * Paso siguiente:
 * Implementar la clase `NetworkResult` para representar de forma segura cada estado posible.
 *
 * @param T Tipo de dato esperado en la respuesta del cuerpo (`Response<T>`).
 * @param block Bloque que ejecuta la llamada Retrofit.
 * @return Instancia de [NetworkResult] representando el resultado.
 */
suspend inline fun <T> safeApiCall(
    crossinline block: suspend () -> Response<T>
): NetworkResult<T> = try {
    // Ejecutamos la llamada Retrofit
    val resp = block()

    // Caso 1: Respuesta HTTP exitosa (2xx)
    if (resp.isSuccessful) {
        val body = resp.body()
        if (body != null) {
            NetworkResult.Success(body)
        } else {
            // Respuesta 200 OK pero sin cuerpo → lo tratamos como error desconocido
            NetworkResult.UnknownError(NullPointerException("Response body is null"))
        }
    } else {
        // Caso 2: Error HTTP controlado (4xx o 5xx)
        NetworkResult.ApiError(
            code = resp.code(),
            message = resp.message(),
            body = resp.errorBody()?.string()
        )
    }
} catch (e: IOException) {
    // Caso 3: Error de red (falta de conexión, timeout, DNS, etc.)
    NetworkResult.NetworkError
} catch (t: Throwable) {
    // Caso 4: Error inesperado (serialización, NullPointer, etc.)
    NetworkResult.UnknownError(t)
}
