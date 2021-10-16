package com.example.ecommerce.domain.product.optiongroup

import com.example.ecommerce.domain.product.option.ProductOption
import javax.persistence.*

@Entity
class ProductOptionGroup(
    name: String,
    order: Int,
    productOptionList: List<ProductOption>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    var name: String = name
        private set

    var order: Int = order
        private set

    @OneToMany
    var productOptionList: List<ProductOption> = productOptionList
        private set


}
