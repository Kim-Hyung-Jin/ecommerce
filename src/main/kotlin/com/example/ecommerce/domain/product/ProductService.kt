package com.example.ecommerce.domain.product

interface ProductService {
    fun registerProduct(command: ProductCommand.RegisterProduct): String
    fun getProduct(criteria: ProductCriteria.GetProduct): ProductInfo.ProductMain
}


