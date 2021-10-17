package com.example.ecommerce.domain.product.option

import javax.persistence.*

@Entity
class ProductOption(
    productOptionName: String,
    ordering: Int,
    optionPrice: Int

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    var productOptionName: String = productOptionName
        private set

    var ordering: Int = ordering
        private set

    var productOptionPrice: Int = optionPrice
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductOption

        if (id != other.id) return false
        if (productOptionName != other.productOptionName) return false
        if (ordering != other.ordering) return false
        if (productOptionPrice != other.productOptionPrice) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + productOptionName.hashCode()
        result = 31 * result + ordering
        result = 31 * result + productOptionPrice
        return result
    }


}
