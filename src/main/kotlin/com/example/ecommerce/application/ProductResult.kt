package com.example.ecommerce.application

import com.example.ecommerce.domain.product.ProductInfo

class ProductResult {
    data class ProductMain(
        val productInfo: ProductInfo.ProductMain
    )
}