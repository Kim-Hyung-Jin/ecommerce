package com.example.ecommerce.domain.product

import com.example.ecommerce.domain.product.optiongroup.ProductOptionGroup
import javax.persistence.*

@Entity
class Product(
    name: String,
    price: Int,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L // TODO 옵셔널 ? 왜 롱 ?

    @OneToMany
    var productOptionGroupList: List<ProductOptionGroup>? = null
        private set

    var name: String = name
        private set

    var price: Int = price
        private set




}