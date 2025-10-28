package com.ceac.mvvmapp.ui.screens.home

import com.ceac.mvvmapp.domain.model.Product

data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
