package com.example.ecommerce.infra.product

import com.example.ecommerce.domain.product.*
import com.example.ecommerce.domain.product.optiongroup.ProductOptionGroup
import org.springframework.stereotype.Component
import javax.persistence.EntityNotFoundException

@Component
class ProductReaderImpl(
    private val productRepository: ProductRepository,
    private val productCommandMapper: ProductCommandMapper
): ProductReader {
    override fun getProductByCode(productCode: String): Product {
        return productRepository.findProductByProductCode(productCode) ?: throw EntityNotFoundException("존재하지 않는 상품입니다.")
    }

    override fun getProductOptionGroupInfoList(product: Product): List<ProductInfo.ProductOptionGroupInfo> {
        return product.productOptionGroupList?.map { productOptionGroup ->
            getProductOptionGroupInfo(productOptionGroup)
        } ?: throw NullPointerException("존재하지 않는 옵션그룹 입니다.") // TODO
    }

    private fun getProductOptionGroupInfo(productOptionGroup: ProductOptionGroup): ProductInfo.ProductOptionGroupInfo {
        val productOptionInfoList = productOptionGroup.productOptionList.map { productOption ->
            productCommandMapper.of(productOption)
        }

        return productCommandMapper.of(productOptionGroup, productOptionInfoList)
    }
}