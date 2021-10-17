package com.example.ecommerce.domain.product

import com.example.ecommerce.domain.product.optiongroup.ProductOptionGroup
import java.util.*
import javax.persistence.*

@Entity
class Product(
    productName: String,
    productPrice: Long,
    productOptionGroupList: List<ProductOptionGroup>?
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L // TODO 옵셔널 ? 왜 롱 ?

    val productCode: String = UUID.randomUUID().toString()

    @OneToMany
    var productOptionGroupList: List<ProductOptionGroup>? = productOptionGroupList
        private set

    var productName: String = productName
        private set

    var productPrice: Long = productPrice
        private set

    @Enumerated(EnumType.STRING)
    var status: Status = Status.PREPARE
        private set

    enum class Status(description: String) {
        PREPARE("판매준비중"), ON_SALE("판매중"), END_OF_SALE("판매종료");
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (productCode != other.productCode) return false
        if (productOptionGroupList != other.productOptionGroupList) return false
        if (productName != other.productName) return false
        if (productPrice != other.productPrice) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + productCode.hashCode()
        result = 31 * result + (productOptionGroupList?.hashCode() ?: 0)
        result = 31 * result + productName.hashCode()
        result = 31 * result + productPrice.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }

    override fun toString(): String {
        return "Product(id=$id, productCode='$productCode', productOptionGroupList=$productOptionGroupList, productName='$productName', productPrice=$productPrice, status=$status)"
    }


}