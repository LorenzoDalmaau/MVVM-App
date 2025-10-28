package com.ceac.mvvmapp.data.repository

import com.ceac.mvvmapp.domain.repository.auth.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ----------------------------------------------------------------------------
 * FakeAuthRepository.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Esta clase representa una **implementación falsa (fake)** del repositorio de autenticación.
 * Se utiliza durante el desarrollo para **simular el comportamiento de un backend real**
 * sin depender de un servidor o una API externa.
 *
 * 🔹 Contexto arquitectónico:
 * Forma parte de la **capa de datos (data layer)** dentro de la arquitectura **MVVM + Clean Architecture**.
 * Implementa la interfaz `AuthRepository` definida en la capa de dominio (`domain`),
 * lo que permite intercambiar fácilmente esta versión "fake" por una real (por ejemplo, una basada en Retrofit o Room)
 * sin modificar el código del ViewModel ni del caso de uso (`LoginUseCase`).
 *
 * 🔹 Uso principal:
 * - Durante la fase de **desarrollo de la UI**, permite probar la app con datos controlados.
 * - Ideal para **tests unitarios** o para **mockear respuestas del backend**.
 *
 * 🔹 Inyección de dependencias:
 * - Está anotada con `@Singleton`, lo que garantiza una única instancia durante el ciclo de vida de la aplicación.
 * - `@Inject constructor()` permite que Hilt la cree automáticamente cuando se solicite una instancia de `AuthRepository`.
 *
 * 🔹 Lógica actual:
 * Este fake valida un único usuario "real":
 *      Email: admin@ceac.com
 *      Password: 1234
 * Si las credenciales coinciden, devuelve `Result.success(Unit)`.
 * En cualquier otro caso, devuelve `Result.failure(Exception("Credenciales incorrectas"))`.
 *
 * 🔹 Próximos pasos (cuando se reemplace):
 * - Sustituir por una implementación real que:
 *      - Haga peticiones HTTP a un backend.
 *      - Gestione tokens JWT.
 *      - Maneje excepciones de red y errores de autenticación.
 * ----------------------------------------------------------------------------
 */
@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {

    /**
     * Simula una llamada de login al backend.
     *
     * @param email    Correo electrónico introducido por el usuario.
     * @param password Contraseña introducida por el usuario.
     * @return Un objeto `Result<Unit>` que representa éxito o error.
     */
    override suspend fun login(email: String, password: String): Result<Unit> {
        return if (email == "admin@ceac.com" && password == "1234") {
            // Simula un login exitoso.
            Result.success(Unit)
        } else {
            // Devuelve un error si las credenciales no son válidas.
            Result.failure(Exception("Credenciales incorrectas"))
        }
    }

    /// TODO Añadir comentarios
    override suspend fun recoverPassword(email: String): Result<Unit> {
        delay(800)
        return if (email.endsWith("@ceac.com")) {
            Result.success(Unit) // Simulamos envío correcto
        } else {
            Result.failure(Exception("No existe ninguna cuenta con ese email"))
        }
    }
}
