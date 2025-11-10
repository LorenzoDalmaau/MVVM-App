package com.ceac.mvvmapp.domain.usecase

import com.ceac.mvvmapp.domain.model.Product
import com.ceac.mvvmapp.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend operator fun invoke(page: Int, size: Int) = repo.getProducts(page, size)
}
