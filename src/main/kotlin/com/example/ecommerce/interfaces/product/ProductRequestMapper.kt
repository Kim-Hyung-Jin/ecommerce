package com.example.ecommerce.interfaces.product

import com.example.ecommerce.application.ProductResult
import com.example.ecommerce.domain.product.ProductCommand
import com.example.ecommerce.domain.product.ProductCriteria
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
interface ProductRequestMapper {

    fun to(request: ProductRequest.RegisterProduct): ProductCommand.RegisterProduct

    @Mappings(
        Mapping(target = "copy", ignore = true)
    )
    fun to(request: ProductRequest.GetProduct): ProductCriteria.GetProduct


    @Mappings(
        Mapping(target = "copy", ignore = true)
    )
    fun of(productCode: String): ProductResponse.RegisterProduct

    @Mappings(
        Mapping(target = "copy", ignore = true),
        Mapping(source = "result.productInfo.productCode", target = "productCode")
    )
    fun of(result: ProductResult.ProductMain): ProductResponse.GetProduct

}
