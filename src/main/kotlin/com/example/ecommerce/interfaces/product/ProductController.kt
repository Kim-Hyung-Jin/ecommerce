package com.example.ecommerce.interfaces.product

import com.example.ecommerce.application.ProductFacade
import org.springframework.stereotype.Controller

@Controller
class ProductController(
    private val productFacade: ProductFacade,
    private val productRequestMapper: ProductRequestMapper,
) {

    fun registerProduct(request: ProductRequest.RegisterProduct): ApiResult<ProductResponse.RegisterProduct> {
        val command = productRequestMapper.to(request)
        val productCode = productFacade.registerProduct(command)
        val response = productRequestMapper.of(productCode)

        return ApiResult.ok(message = "상품 등록 완료", data = response)
    }

    fun getProduct(request: ProductRequest.GetProduct): ApiResult<ProductResponse.GetProduct> {
        val criteria = productRequestMapper.to(request)
        val productResult = productFacade.getProduct(criteria)
        val response = productRequestMapper.of(productResult)

        return ApiResult.ok(message = "상품 조회 완료", data = response)
    }
}