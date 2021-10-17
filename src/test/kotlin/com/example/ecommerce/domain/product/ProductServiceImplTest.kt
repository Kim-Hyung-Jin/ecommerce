package com.example.ecommerce.domain.product

import com.appmattus.kotlinfixture.kotlinFixture
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

    private val fixture = kotlinFixture()


    init {
        "상픙 등록 요청 시" - {
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

                val productOptionGroupList = listOf(granolaProductOptionGroup, honeyProductOptionGroup) // TODO ordering 중복되는경우

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

        "상품 조회 요청 시" - {
            "상품 코드를 입력하면" - {
                val criteria = ProductCriteria.GetProduct(
                    productCode = UUID.randomUUID().toString()
                )

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

                val productOptionGroupList = listOf(granolaProductOptionGroup, honeyProductOptionGroup) // ordering 중복되는경우


                "상품 정보 응답" - {
                    val mockUUID = UUID.randomUUID()
                    Mockito.mockStatic(UUID::class.java).use { mockedUuid ->
                        mockedUuid.`when`<Any> { UUID.randomUUID().toString() }.thenReturn(mockUUID)

                        val product = Product(
                            productName = fixture<String>(),
                            productPrice = fixture<Long>(),
                            productOptionGroupList = productOptionGroupList // TODO 어떤식으로 할지
                        )
                        val productCode = mockUUID.toString()
                        val status = fixture<Product.Status>()
                        ReflectionTestUtils.setField(product, "status", status)
                        ReflectionTestUtils.setField(product, "productCode", productCode)

                        val granolaProductOptionInfo = ProductInfo.ProductOptionInfo(
                            productOptionName = granolaProductOption.productOptionName,
                            ordering = granolaProductOption.ordering,
                            productOptionPrice = granolaProductOption.productOptionPrice
                        )

                        val granolaProductOptionInfo2 = ProductInfo.ProductOptionInfo(
                            productOptionName = granolaProductOption2.productOptionName,
                            ordering = granolaProductOption2.ordering,
                            productOptionPrice = granolaProductOption2.productOptionPrice
                        )

                        val granolaProductOptionInfo3 = ProductInfo.ProductOptionInfo(
                            productOptionName = granolaProductOption3.productOptionName,
                            ordering = granolaProductOption3.ordering,
                            productOptionPrice = granolaProductOption3.productOptionPrice
                        )

                        val granolaProductOptionGroupInfo = ProductInfo.ProductOptionGroupInfo(
                            productOptionGroupName = granolaProductOptionGroup.productOptionGroupName,
                            ordering = granolaProductOptionGroup.ordering,
                            productOptionList = listOf(
                                granolaProductOptionInfo,
                                granolaProductOptionInfo2,
                                granolaProductOptionInfo3
                            )
                        )

                        val honeyProductOptionInfo = ProductInfo.ProductOptionInfo(
                            productOptionName = honeyProductOption.productOptionName,
                            ordering = honeyProductOption.ordering,
                            productOptionPrice = honeyProductOption.productOptionPrice
                        )

                        val honeyProductOptionInfo2 = ProductInfo.ProductOptionInfo(
                            productOptionName = honeyProductOption2.productOptionName,
                            ordering = honeyProductOption2.ordering,
                            productOptionPrice = honeyProductOption2.productOptionPrice
                        )

                        val honeyProductOptionInfo3 = ProductInfo.ProductOptionInfo(
                            productOptionName = honeyProductOption3.productOptionName,
                            ordering = honeyProductOption3.ordering,
                            productOptionPrice = honeyProductOption3.productOptionPrice
                        )

                        val honeyProductOptionGroupInfo = ProductInfo.ProductOptionGroupInfo(
                            productOptionGroupName = honeyProductOptionGroup.productOptionGroupName,
                            ordering = honeyProductOptionGroup.ordering,
                            productOptionList = listOf(honeyProductOptionInfo, honeyProductOptionInfo2, honeyProductOptionInfo3)
                        )

                        val productOptionGroupInfoList = listOf(granolaProductOptionGroupInfo, honeyProductOptionGroupInfo)

                        val expectedInfo = ProductInfo.ProductMain(
                            productName = product.productName,
                            productPrice = product.productPrice,
                            productCode = productCode,
                            status = product.status,
                            productOptionGroupList = productOptionGroupInfoList
                        )

                        Mockito.`when`(productReader.getProductByCode(criteria.productCode)).thenReturn(product)
                        Mockito.`when`(productReader.getProductOptionGroupInfoList(product)).thenReturn(listOf(granolaProductOptionGroupInfo, honeyProductOptionGroupInfo))

                        val res = productService.getProduct(criteria)
                        Assertions.assertThat(res).isEqualTo(expectedInfo)
                    }

                }
            }
        }
    }
}
