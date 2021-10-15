package com.example.ecommerce.infra.product

import com.appmattus.kotlinfixture.kotlinFixture
import com.example.ecommerce.domain.product.Product
import com.example.ecommerce.domain.product.ProductCommandMapperImpl
import com.example.ecommerce.domain.product.ProductRepository
import com.example.ecommerce.domain.product.ProductStore
import com.example.ecommerce.domain.product.option.ProductOption
import com.example.ecommerce.domain.product.optiongroup.ProductOptionGroup
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.FreeSpec
import io.kotest.spring.SpringListener
import org.assertj.core.api.Assertions
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

//@ActiveProfiles(value = ["test"])
@SpringBootTest(classes = [ProductStoreImpl::class, ProductCommandMapperImpl::class])
class ProductStoreImplTest : FreeSpec() {
    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    @Autowired
    private lateinit var productStore: ProductStore

    @MockBean
    private lateinit var productRepository: ProductRepository

    private val fixture = kotlinFixture()

    init {
        "상품 저장 요청시" - {
            "추가 옵션이 주어졌을 때" - {

                val granolaProductOption = ProductOption(
                    productOptionName = "그래놀라 500g",
                    ordering = 1,
                    productOptionPrice = 5000
                )

                val granolaProductOption2 = ProductOption(
                    productOptionName = "그래놀라 1kg",
                    ordering = 2,
                    productOptionPrice = 10000
                )

                val granolaProductOption3 = ProductOption(
                    productOptionName = "그래놀라 3kg",
                    ordering = 3,
                    productOptionPrice = 2000
                )

                val granolaProductOptionGroup = ProductOptionGroup(
                    productOptionGroupName = "그래놀라 추가",
                    ordering = 1,
                    productOptionList = listOf(granolaProductOption, granolaProductOption2, granolaProductOption3)
                )

                val honeyProductOption = ProductOption(
                    productOptionName = "꿀 500g",
                    ordering = 1,
                    productOptionPrice = 3000
                )

                val honeyProductOption2 = ProductOption(
                    productOptionName = "꿀 1kg",
                    ordering = 1,
                    productOptionPrice = 5500
                )

                val honeyProductOption3 = ProductOption(
                    productOptionName = "꿀 3kg",
                    ordering = 3,
                    productOptionPrice = 145000
                )

                val honeyProductOptionGroup = ProductOptionGroup(
                    productOptionGroupName = "꿀 추가",
                    ordering = 2,
                    productOptionList = listOf(honeyProductOption, honeyProductOption2, honeyProductOption3)
                )

                val productOptionGroupList = listOf(granolaProductOptionGroup, honeyProductOptionGroup)


                "정상적으로 상품 코드를 응답" - {
                    val mockUUID = UUID.randomUUID()
                    Mockito.mockStatic(UUID::class.java).use { mockedUuid ->
                        mockedUuid.`when`<Any> { UUID.randomUUID().toString() }.thenReturn(mockUUID)

                        val productCode = mockUUID.toString()
                        val status = fixture<Product.Status>()
                        val entity = Product(
                            productName = fixture<String>(),
                            productPrice = fixture<Long>(),
                            productOptionGroupList = productOptionGroupList
                        )
                        ReflectionTestUtils.setField(entity, "status", status)
                        ReflectionTestUtils.setField(entity, "productCode", productCode)

                        val expectedEntity = Product(
                            productName = entity.productName,
                            productPrice = entity.productPrice,
                            productOptionGroupList = entity.productOptionGroupList
                        )
                        ReflectionTestUtils.setField(expectedEntity, "status", status)
                        ReflectionTestUtils.setField(expectedEntity, "productCode", productCode)


                        Mockito.`when`(productRepository.save(entity))
                            .thenReturn(expectedEntity)

                        val res = productStore.store(expectedEntity)
                        Assertions.assertThat(res).isEqualTo(expectedEntity)
                    }
                }

            }
            "추가 옵션이 없을 때" - {
                "정상적으로 상품 코드를 응답" - {
                    val mockUUID = UUID.randomUUID()
                    Mockito.mockStatic(UUID::class.java).use { mockedUuid ->
                        mockedUuid.`when`<Any> { UUID.randomUUID().toString() }.thenReturn(mockUUID)

                        val productCode = mockUUID.toString()
                        val status = fixture<Product.Status>()
                        val entity = Product(
                            productName = fixture<String>(),
                            productPrice = fixture<Long>(),
                            productOptionGroupList = null
                        )
                        ReflectionTestUtils.setField(entity, "status", status)
                        ReflectionTestUtils.setField(entity, "productCode", productCode)

                        val expectedEntity = Product(
                            productName = entity.productName,
                            productPrice = entity.productPrice,
                            productOptionGroupList = entity.productOptionGroupList
                        )
                        ReflectionTestUtils.setField(expectedEntity, "status", status)
                        ReflectionTestUtils.setField(expectedEntity, "productCode", productCode)


                        Mockito.`when`(productRepository.save(entity))
                            .thenReturn(expectedEntity)

                        val res = productStore.store(expectedEntity)
                        Assertions.assertThat(res).isEqualTo(expectedEntity)
                    }
                }

            }
        }
    }
}
