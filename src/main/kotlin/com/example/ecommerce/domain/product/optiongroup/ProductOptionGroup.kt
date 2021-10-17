package com.example.ecommerce.domain.product.optiongroup

import com.example.ecommerce.domain.product.option.ProductOption
import javax.persistence.*

@Entity
class ProductOptionGroup(
    productOptionGroupName: String,
    ordering: Int,
    productOptionList: List<ProductOption>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    var productOptionGroupName: String = productOptionGroupName
        private set

    var ordering: Int = ordering
        private set

    @OneToMany
    var productOptionList: List<ProductOption> = productOptionList
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductOptionGroup

        if (id != other.id) return false
        if (productOptionGroupName != other.productOptionGroupName) return false
        if (ordering != other.ordering) return false
        if (productOptionList != other.productOptionList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + productOptionGroupName.hashCode()
        result = 31 * result + ordering
        result = 31 * result + productOptionList.hashCode()
        return result
    }


}
