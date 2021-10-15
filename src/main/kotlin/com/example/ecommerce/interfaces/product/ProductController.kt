package com.example.ecommerce.interfaces.product

import com.example.ecommerce.application.ProductFacade
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productFacade: ProductFacade,
    private val productRequestMapper: ProductRequestMapper,
) {

    @PostMapping("")
    fun registerProduct(@RequestBody request: ProductRequest.RegisterProduct): ApiResult<ProductResponse.RegisterProduct> {
        val command = productRequestMapper.to(request)
        val productCode = productFacade.registerProduct(command)
        val response = productRequestMapper.of(productCode)

        return ApiResult.ok(message = "상품 등록 완료", data = response)
    }

    @GetMapping("/{productCode}")
    fun getProduct(@PathVariable(value = "productCode") productCode: String): ApiResult<ProductResponse.GetProduct> {
        val criteria = productRequestMapper.to(productCode)
        val productResult = productFacade.getProduct(criteria)
        val response = productRequestMapper.of(productResult)

        return ApiResult.ok(message = "상품 조회 완료", data = response)
    }
}