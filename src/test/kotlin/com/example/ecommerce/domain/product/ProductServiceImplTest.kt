package com.example.ecommerce.domain.product

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
@SpringBootTest(classes = [ProductServiceImpl::class, ProductCommandMapperImpl::class])
class ProductServiceImplTest : FreeSpec(){
    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    @Autowired
    private lateinit var productService: ProductService

    @MockBean
    private lateinit var productReader: ProductReader

    @MockBean
    private lateinit var productStore: ProductStore


    init {
        "상픙 등록 요청시" - {
            "추가 옵션이 주어졌을 때" - {

                val granolaProductOption = ProductOption(
                    productOptionName = "그래놀라 500g",
                    ordering = 1,
                    optionPrice = 5000
                )

                val granolaProductOption2 = ProductOption(
                    productOptionName = "그래놀라 1kg",
                    ordering = 1,
                    optionPrice = 10000
                )

                val granolaProductOption3 = ProductOption(
                    productOptionName = "그래놀라 3kg",
                    ordering = 3,
                    optionPrice = 2000
                )

                val granolaProductOptionGroup = ProductOptionGroup(
                    productOptionGroupName = "그래놀라 추가",
                    ordering = 1,
                    productOptionList = listOf(granolaProductOption, granolaProductOption2, granolaProductOption3)
                )

                val honeyProductOption = ProductOption(
                    productOptionName = "꿀 500g",
                    ordering = 1,
                    optionPrice = 3000
                )

                val honeyProductOption2 = ProductOption(
                    productOptionName = "꿀 1kg",
                    ordering = 1,
                    optionPrice = 5500
                )

                val honeyProductOption3 = ProductOption(
                    productOptionName = "꿀 3kg",
                    ordering = 3,
                    optionPrice = 145000
                )

                val honeyProductOptionGroup = ProductOptionGroup(
                    productOptionGroupName = "꿀 추가",
                    ordering = 2,
                    productOptionList = listOf(honeyProductOption, honeyProductOption2, honeyProductOption3)
                )

                val productOptionGroupList = listOf(granolaProductOptionGroup, honeyProductOptionGroup) // ordering 중복되는경우

                val command = ProductCommand.RegisterProduct(
                    productName = "그릭요거트 500g",
                    productPrice = 20000,
                    productOptionGroupList = productOptionGroupList
                )


                "정상적으로 상품 코드를 응답" - {
                    val mockUUID = UUID.randomUUID()
                    Mockito.mockStatic(UUID::class.java).use { mockedUuid ->
                        mockedUuid.`when`<Any> { UUID.randomUUID().toString() }.thenReturn(mockUUID)

                        val productCode = mockUUID.toString()
                        val status = Product.Status.PREPARE
                        val entity = Product(
                            productName = command.productName,
                            productPrice = command.productPrice,
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

                        Mockito.`when`(productStore.store(expectedEntity)).thenReturn(expectedEntity)

                        val res = productService.registerProduct(command)
                        Assertions.assertThat(res).isEqualTo(productCode)
                    }
                }
            }

            "추가 옵션 없을 때" - {
                val command = ProductCommand.RegisterProduct(
                    productName = "그릭요거트 500g",
                    productPrice = 20000,
                    productOptionGroupList = null
                )

                "정상적으로 상품 코드를 응답" - {
                    val mockUUID = UUID.randomUUID()
                    Mockito.mockStatic(UUID::class.java).use { mockedUuid ->
                        mockedUuid.`when`<Any> { UUID.randomUUID().toString() }.thenReturn(mockUUID)

                        val productCode = mockUUID.toString()
                        val status = Product.Status.PREPARE
                        val entity = Product(
                            productName = command.productName,
                            productPrice = command.productPrice,
                            productOptionGroupList = null
                        )
                        ReflectionTestUtils.setField(entity, "status", status)
                        ReflectionTestUtils.setField(entity, "productCode", productCode)

                        val expectedEntity = Product(
                            productName = entity.productName,
                            productPrice = entity.productPrice,
                            productOptionGroupList = null
                        )
                        ReflectionTestUtils.setField(expectedEntity, "status", status)
                        ReflectionTestUtils.setField(expectedEntity, "productCode", productCode)

                        Mockito.`when`(productStore.store(expectedEntity)).thenReturn(expectedEntity)

                        val res = productService.registerProduct(command)
                        Assertions.assertThat(res).isEqualTo(productCode)
                    }
                }
            }
        }
    }
}
