package com.example.ecommerce.domain.order

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L // TODO 옵셔널 ? 왜 롱 ?


}