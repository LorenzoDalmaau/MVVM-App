package com.ceac.mvvmapp.di

import com.ceac.mvvmapp.domain.repository.auth.AuthRepository
import com.ceac.mvvmapp.data.repository.FakeAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * ----------------------------------------------------------------------------
 * AppModule.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Este archivo define un **módulo de inyección de dependencias (DI)** para Hilt.
 * Su objetivo es decirle a Hilt **cómo proporcionar las implementaciones concretas**
 * cuando una clase (por ejemplo, un ViewModel o un UseCase) necesita una interfaz.
 *
 * 🔹 Contexto arquitectónico:
 * - Pertenece a la **capa de inyección de dependencias (di)** del proyecto.
 * - Aquí se establecen los **enlaces (bindings)** entre las interfaces de dominio
 *   y sus implementaciones concretas de la capa de datos.
 * - Esto permite cumplir el **principio de inversión de dependencias (D de SOLID)**:
 *   las capas superiores dependen de abstracciones, no de implementaciones.
 *
 * 🔹 Funcionamiento:
 * 1. `@Module`: indica que esta clase contiene instrucciones para construir dependencias.
 * 2. `@InstallIn(SingletonComponent::class)`:
 *     - Define el **alcance (scope)** del módulo.
 *     - En este caso, las dependencias vivirán **durante toda la aplicación** (Singleton).
 * 3. `@Binds`: le dice a Hilt qué implementación usar cuando alguien solicite una interfaz.
 *     - Aquí se indica que siempre que se solicite `AuthRepository`,
 *       se inyecte una instancia de `FakeAuthRepository`.
 * 4. `@Singleton`: asegura que se use **una única instancia** compartida en toda la app.
 *
 * 🔹 Ejemplo de flujo:
 * - `LoginUseCase` solicita un `AuthRepository`.
 * - Hilt busca en los módulos registrados cómo crearlo.
 * - Encuentra este binding → devuelve una instancia de `FakeAuthRepository`.
 *
 * 🔹 Próximos pasos (cuando haya backend real):
 * - Reemplazar `FakeAuthRepository` por una implementación real (por ejemplo, `AuthRepositoryImpl`)
 *   que consuma una API mediante Retrofit o use una base de datos local (Room).
 *
 * ----------------------------------------------------------------------------
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Enlaza la interfaz `AuthRepository` con su implementación concreta `FakeAuthRepository`.
     *
     * @param impl Implementación que se inyectará cuando se solicite `AuthRepository`.
     * @return Una instancia lista para usar de tipo `AuthRepository`.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: FakeAuthRepository
    ): AuthRepository
}
