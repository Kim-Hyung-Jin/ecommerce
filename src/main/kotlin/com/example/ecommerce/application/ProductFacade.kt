package com.example.ecommerce.application

import com.example.ecommerce.domain.product.ProductCommand
import com.example.ecommerce.domain.product.ProductCommandMapper
import com.example.ecommerce.domain.product.ProductCriteria
import com.example.ecommerce.domain.product.ProductService
import org.springframework.stereotype.Service

@Service
class ProductFacade(
    private val productService: ProductService,
    private val productCommandMapper: ProductCommandMapper
) {
    fun registerProduct(command: ProductCommand.RegisterProduct): String {
        return productService.registerProduct(command)
    }

    fun getProduct(criteria: ProductCriteria.GetProduct): ProductResult.ProductMain {
        val productInfo = productService.getProduct(criteria)
        return productCommandMapper.of(productInfo)
    }
}