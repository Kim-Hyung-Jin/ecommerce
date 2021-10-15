package com.example.ecommerce.domain.product

class ProductInfo {
    data class ProductMain(
        val productName: String,
        val productPrice: Long,
        val productCode: String,
        val status: Product.Status,
        val productOptionGroupList: List<ProductOptionGroupInfo>?
    )

    data class ProductOptionGroupInfo(
        val productOptionGroupName: String,
        val ordering: Int,
        val productOptionList: List<ProductOptionInfo>
    )

    data class ProductOptionInfo(
        val productOptionName: String,
        val productOptionPrice: Long,
        val ordering: Int,
    )
}


