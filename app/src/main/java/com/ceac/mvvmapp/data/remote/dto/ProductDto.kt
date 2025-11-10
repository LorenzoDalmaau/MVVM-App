package com.ceac.mvvmapp.data.remote.dto

import com.ceac.mvvmapp.domain.model.Product
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Paso 4.5: DTO de producto.
 *
 * Explicación:
 * Representa la forma en que el backend entrega un producto.
 * Debe reflejar exactamente el contrato JSON del servidor.
 *
 * Consideraciones:
 * - Usa @Json(name = "...") si los nombres del backend no coinciden.
 * - Mantén tipos alineados (si el backend usa decimales de alta precisión,
 *   valora BigDecimal en DTO y mapea a Double/BigDecimal en dominio).
 */
@JsonClass(generateAdapter = true)
data class ProductDto(
    /** Identificador único del producto. */
    @Json(name = "id") val id: String,
    /** Nombre comercial del producto. */
    @Json(name = "name") val name: String,
    /** Descripción resumida del producto. */
    @Json(name = "description") val description: String,
    /** Precio unitario; asegúrate de la precisión esperada. */
    @Json(name = "price") val price: Double,
    /** URL absoluta o relativa de la imagen. */
    @Json(name = "imageUrl") val imageUrl: String
)

/**
 * Paso 4.6: Mapeo DTO → Dominio.
 *
 * Explicación:
 * Convierte el objeto de transporte (ProductDto) en el modelo de dominio (Product),
 * evitando que detalles del backend "contaminen" la capa de dominio/UI.
 *
 * @receiver ProductDto Objeto recibido desde la capa de red.
 * @return Product Modelo de dominio utilizado por casos de uso y UI.
 */
fun ProductDto.toDomain(): Product = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl
)

/**
 * Paso siguiente:
 * - Declarar ProductApi con Retrofit (GET /api/v1/products?page=&size=).
 * - Implementar ProductRepositoryImpl usando safeApiCall y toDomain().
 * - Gestionar paginación en el ViewModel (page/size) y exponer HomeUiState.
 */
