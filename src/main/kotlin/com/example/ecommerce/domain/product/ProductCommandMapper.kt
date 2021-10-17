package com.example.ecommerce.domain.product

import com.example.ecommerce.application.ProductResult
import com.example.ecommerce.domain.product.option.ProductOption
import com.example.ecommerce.domain.product.optiongroup.ProductOptionGroup
import org.mapstruct.*


@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
interface ProductCommandMapper {
    @Mappings(
        Mapping(target = "status", ignore = true)
    )
    fun to(command: ProductCommand.RegisterProduct): Product


    @Mappings(
        Mapping(source = "info", target = "productInfo"),
        Mapping(target = "copy", ignore = true)
    )
    fun of(info: ProductInfo.ProductMain): ProductResult.ProductMain

    @Mappings(
        Mapping(source = "productOptionGroupInfoList", target = "productOptionGroupList"),
    )
    fun of(
        entity: Product,
        productOptionGroupInfoList: List<ProductInfo.ProductOptionGroupInfo>
    ): ProductInfo.ProductMain

    @Mappings(
        Mapping(source = "productOptionInfoList", target = "productOptionList")
    )
    fun of(
        entity: ProductOptionGroup,
        productOptionInfoList: List<ProductInfo.ProductOptionInfo>
    ): ProductInfo.ProductOptionGroupInfo

    fun of(entity: ProductOption): ProductInfo.ProductOptionInfo
}