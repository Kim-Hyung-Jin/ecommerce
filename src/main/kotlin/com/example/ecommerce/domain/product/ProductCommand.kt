package com.example.ecommerce.domain.product

import com.example.ecommerce.domain.product.optiongroup.ProductOptionGroup

class ProductCommand {
    data class RegisterProduct(
        val productName: String,
        val productPrice: Long,
        val productOptionGroupList: List<ProductOptionGroup>?
    )
}
