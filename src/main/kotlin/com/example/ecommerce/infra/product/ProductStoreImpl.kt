package com.example.ecommerce.infra.product

import com.example.ecommerce.domain.product.Product
import com.example.ecommerce.domain.product.ProductRepository
import com.example.ecommerce.domain.product.ProductStore
import org.springframework.stereotype.Component

@Component
class ProductStoreImpl(
    private val productRepository: ProductRepository
): ProductStore {
    override fun store(product: Product): Product {
        return productRepository.save(product)
    }
}