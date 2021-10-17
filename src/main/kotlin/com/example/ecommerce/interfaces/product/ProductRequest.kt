package com.example.ecommerce.interfaces.product

import com.example.ecommerce.domain.product.optiongroup.ProductOptionGroup

class ProductRequest {
    data class RegisterProduct(
        val productName: String,
        val productPrice: Long,
        val productOptionGroupList: List<ProductOptionGroup>?
    )

    data class GetProduct(
        val productCode: String,
    )
}
