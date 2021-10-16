package com.example.ecommerce.domain.product.option

import javax.persistence.*

@Entity
class ProductOption(
    name: String,
    order: Int,
    optionPrice: Int

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    var name: String = name
        private set

    var order: Int = order
        private set

    var optionPrice: Int = optionPrice
        private set


}
