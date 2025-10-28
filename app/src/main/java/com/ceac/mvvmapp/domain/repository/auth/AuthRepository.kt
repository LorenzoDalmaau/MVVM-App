package com.ceac.mvvmapp.domain.repository.auth

/**
 * ----------------------------------------------------------------------------
 * AuthRepository.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Esta interfaz define el **contrato del repositorio de autenticación**.
 * Su propósito es declarar qué operaciones relacionadas con la autenticación
 * estarán disponibles para el resto de la aplicación, **sin importar cómo se implementen**.
 *
 * 🔹 Contexto arquitectónico:
 * - Pertenece a la **capa de dominio (domain layer)** dentro del patrón
 *   **Clean Architecture / MVVM**.
 * - El dominio contiene únicamente **lógica de negocio pura** y **abstracciones**,
 *   nunca implementaciones concretas ni dependencias de frameworks.
 *
 * 🔹 Responsabilidad:
 * - Esta interfaz **aísla la lógica de negocio** de los detalles de infraestructura (API, DB...).
 * - Las capas superiores (como los casos de uso o los ViewModels) dependen de esta interfaz,
 *   y no de cómo realmente se realiza el login.
 * - Esto permite cambiar fácilmente la fuente de datos (de una API real a una fake, o viceversa)
 *   sin alterar el resto del código.
 *
 * 🔹 Patrón aplicado:
 * - Este enfoque sigue el **Principio de Inversión de Dependencias (D de SOLID)**:
 *   las clases de alto nivel (use cases, viewmodels) dependen de abstracciones,
 *   no de implementaciones concretas.
 *
 * 🔹 Ejemplo de implementación:
 * - En desarrollo: `FakeAuthRepository` (sin servidor real).
 * - En producción: `AuthRepositoryImpl` (por ejemplo, usando Retrofit o Firebase).
 *
 * ----------------------------------------------------------------------------
 */
interface AuthRepository {

    /**
     * Realiza el proceso de autenticación de usuario.
     *
     * @param email    Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Un objeto `Result<Unit>` que indica éxito o fallo en la autenticación.
     *
     * - `Result.success(Unit)` → Login exitoso.
     * - `Result.failure(Exception)` → Error en las credenciales o en la conexión.
     *
     * Este método es `suspend` porque implica una **operación asíncrona**,
     * normalmente una llamada de red o una operación de I/O.
     */
    suspend fun login(email: String, password: String): Result<Unit>

    /**
     * TODO Implementar comentarios
     */
    suspend fun recoverPassword(email: String): Result<Unit>

    /**
     * TODO Implementar comentarios
     */
    suspend fun register(email: String, password: String): Result<Unit>
}
