package com.example.ecommerce.domain.product.option

interface ProductOptionStore {
    fun store(productOption: ProductOption): ProductOption
}