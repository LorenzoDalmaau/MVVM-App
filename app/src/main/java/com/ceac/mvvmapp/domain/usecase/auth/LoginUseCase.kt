package com.ceac.mvvmapp.domain.usecase.auth

import com.ceac.mvvmapp.domain.repository.auth.AuthRepository
import javax.inject.Inject

/**
 * ----------------------------------------------------------------------------
 * LoginUseCase.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Este caso de uso encapsula la **lógica de negocio asociada al inicio de sesión (login)**.
 * En lugar de que el ViewModel interactúe directamente con el repositorio,
 * delega esta responsabilidad en un *use case* (caso de uso).
 *
 * 🔹 Contexto arquitectónico:
 * - Pertenece a la **capa de dominio (domain layer)** dentro de la arquitectura **Clean Architecture**.
 * - En esta capa se definen las **reglas de negocio puras**, independientes de frameworks
 *   o detalles de infraestructura (por ejemplo, Retrofit, Room o Firebase).
 * - Un *UseCase* representa una **acción o proceso de negocio concreto**, en este caso “iniciar sesión”.
 *
 * 🔹 Responsabilidad:
 * - Actuar como intermediario entre el `ViewModel` y el `AuthRepository`.
 * - Aplicar validaciones simples, transformaciones o preprocesamientos si fuese necesario.
 * - Mantener la lógica de negocio **fuera de la UI**.
 *
 * 🔹 Inyección de dependencias:
 * - Se usa `@Inject` para que Hilt pueda proveer automáticamente una instancia de `AuthRepository`
 *   cuando se cree el `LoginUseCase`.
 *
 * 🔹 Operador `invoke`:
 * - Permite ejecutar el caso de uso como si fuera una función directamente:
 *   ```kotlin
 *   val result = loginUseCase(email, password)
 *   ```
 * - Mejora la legibilidad del código en el ViewModel.
 *
 * 🔹 Lógica actual:
 * - Elimina posibles espacios en blanco del email (`trim()`).
 * - Llama al método `login()` del repositorio y devuelve su resultado.
 *
 * 🔹 Ejemplo de flujo:
 * ```
 * LoginViewModel → LoginUseCase → AuthRepository → Backend (o FakeRepo)
 * ```
 *
 * ----------------------------------------------------------------------------
 */
class LoginUseCase @Inject constructor(
    private val repo: AuthRepository
) {

    /**
     * Ejecuta el proceso de login a través del repositorio de autenticación.
     *
     * @param email    Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Un objeto `Result<Unit>` con el resultado del intento de login.
     *
     * - Si las credenciales son válidas, devuelve `Result.success(Unit)`.
     * - Si son incorrectas o hay un error, devuelve `Result.failure(Exception)`.
     */
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repo.login(email.trim(), password)
}
