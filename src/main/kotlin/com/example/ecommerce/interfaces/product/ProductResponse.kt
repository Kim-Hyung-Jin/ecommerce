package com.example.ecommerce.interfaces.product

class ProductResponse {
    data class RegisterProduct(
        val productCode: String
    )

    data class GetProduct(
        val productCode: String
    )
}