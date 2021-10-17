package com.example.ecommerce.domain.product

interface ProductReader {
    fun getProductByCode(productCode: String): Product
    fun getProductOptionGroupInfoList(product: Product): List<ProductInfo.ProductOptionGroupInfo>
}