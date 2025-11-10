package com.ceac.mvvmapp.data.repository

import com.ceac.mvvmapp.data.remote.api.ProductApi
import com.ceac.mvvmapp.data.remote.dto.toDomain
import com.ceac.mvvmapp.data.util.NetworkResult
import com.ceac.mvvmapp.data.util.safeApiCall
import com.ceac.mvvmapp.domain.model.Product
import com.ceac.mvvmapp.domain.repository.ProductRepository
import java.io.IOException
import javax.inject.Inject

/**
 * Paso 8: Implementación del repositorio de productos.
 *
 * Explicación:
 * Esta clase es la responsable de conectar el dominio de la aplicación con
 * el origen de datos remoto (API REST).
 * En la arquitectura Clean, los **repositories** sirven como una capa
 * intermedia que abstrae la fuente de datos y traduce los resultados
 * a modelos del dominio.
 *
 * Este repositorio utiliza:
 * - [ProductApi]: interfaz Retrofit que define los endpoints de productos.
 * - [safeApiCall]: función de utilidad que encapsula los errores de red.
 * - [NetworkResult]: tipo sellado que representa el resultado tipado de una petición HTTP.
 * - [toDomain]: función de extensión que convierte DTOs en modelos de dominio.
 *
 * Resultado:
 * Devuelve un `Result<List<Product>>`, para que los casos de uso o ViewModels
 * puedan manejar el éxito o el error sin preocuparse de los detalles de red.
 *
 * Paso siguiente:
 * Inyectar este repositorio en el caso de uso `GetProductsUseCase` para exponerlo
 * al dominio y luego al `HomeViewModel`.
 */
class ProductRepositoryImpl @Inject constructor(
    /** Cliente Retrofit que realiza las llamadas HTTP al backend. */
    private val api: ProductApi
) : ProductRepository {

    // -------------------------------------------------------------------------
    // Obtener productos
    // -------------------------------------------------------------------------

    /**
     * Recupera una lista de productos desde el backend remoto.
     *
     * @param page Página actual de resultados (para paginación).
     * @param size Número de elementos por página.
     * @return `Result<List<Product>>` con los datos en caso de éxito o una excepción en caso de fallo.
     *
     * Flujo de ejecución:
     * 1. Llama al endpoint remoto `GET /products?page={page}&size={size}`.
     * 2. Usa [safeApiCall] para envolver la llamada y manejar errores HTTP o de red.
     * 3. Si la respuesta es exitosa, convierte los DTO a modelos del dominio usando `toDomain()`.
     * 4. Retorna `Result.success()` o `Result.failure()` según el caso.
     *
     * Ejemplo de uso:
     * ```
     * val result = productRepository.getProducts(page = 0, size = 10)
     * result.onSuccess { products -> println("Productos: $products") }
     * result.onFailure { println("Error: ${it.message}") }
     * ```
     */
    override suspend fun getProducts(page: Int, size: Int): Result<List<Product>> {
        return when (val res = safeApiCall { api.getProducts(page, size) }) {

            // Éxito: convertimos los DTOs en entidades de dominio
            is NetworkResult.Success -> {
                val domainProducts = res.data.map { it.toDomain() }
                Result.success(domainProducts)
            }

            // Error HTTP controlado (por ejemplo 404 o 500)
            is NetworkResult.ApiError -> {
                Result.failure(Exception("API ${res.code}: ${res.message}"))
            }

            // Error de red (sin conexión, timeout, etc.)
            is NetworkResult.NetworkError -> {
                Result.failure(IOException("Network error"))
            }

            // Error inesperado (parseo, null pointer, etc.)
            is NetworkResult.UnknownError -> {
                Result.failure(res.throwable)
            }
        }
    }
}
