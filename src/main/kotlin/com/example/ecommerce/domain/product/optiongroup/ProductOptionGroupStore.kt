package com.example.ecommerce.domain.product.optiongroup

interface ProductOptionGroupStore {
    fun store(productOptionGroup: ProductOptionGroup): ProductOptionGroup
}