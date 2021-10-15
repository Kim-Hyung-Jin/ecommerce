package com.example.ecommerce.domain.product

interface ProductStore {
    fun store(product: Product): Product
}