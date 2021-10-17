package com.example.ecommerce.domain.product

import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productReader: ProductReader,
    private val productStore: ProductStore,
    private val productCommandMapper: ProductCommandMapper
): ProductService {
    override fun registerProduct(command: ProductCommand.RegisterProduct): String {
        val initProduct = productCommandMapper.to(command)
        val product = productStore.store(initProduct)

        return product.productCode
    }

    override fun getProduct(criteria: ProductCriteria.GetProduct): ProductInfo.ProductMain {
        val product = productReader.getProductByCode(criteria.productCode)
        val productOptionGroupInfoList = productReader.getProductOptionGroupInfoList(product)

        return productCommandMapper.of(product, productOptionGroupInfoList)
    }
}