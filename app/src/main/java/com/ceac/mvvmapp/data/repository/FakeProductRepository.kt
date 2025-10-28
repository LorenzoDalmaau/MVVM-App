package com.ceac.mvvmapp.data.repository

import com.ceac.mvvmapp.domain.model.Product
import com.ceac.mvvmapp.domain.repository.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

@Singleton
class FakeProductRepository @Inject constructor() : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        // Simula latencia de red
        delay(600)

        // Lista mock con imágenes de Picsum (seed -> estable)
        return (1..100).map { i ->
            Product(
                id = i.toString(),
                name = "Producto $i",
                description = "Descripción breve del producto $i. Calidad top.",
                price = (10..99).random() + 0.99,
                imageUrl = "https://picsum.photos/seed/product_$i/600/400"
            )
        }
    }
}
