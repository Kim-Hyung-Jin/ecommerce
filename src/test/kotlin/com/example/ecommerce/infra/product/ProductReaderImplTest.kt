package com.example.ecommerce.infra.product

import com.appmattus.kotlinfixture.kotlinFixture
import com.example.ecommerce.domain.product.*
import com.example.ecommerce.domain.product.option.ProductOption
import com.example.ecommerce.domain.product.optiongroup.ProductOptionGroup
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.FreeSpec
import io.kotest.spring.SpringListener
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.util.ReflectionTestUtils
import java.util.*
import javax.persistence.EntityNotFoundException

//@ActiveProfiles(value = ["test"])
@SpringBootTest(classes = [ProductReaderImpl::class, ProductCommandMapperImpl::class])
class ProductReaderImplTest : FreeSpec(){
    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    @Autowired
    private lateinit var productReader: ProductReader

    @MockBean
    private lateinit var productRepository: ProductRepository

    private val fixture = kotlinFixture()

    init {
        "상픙코드로 상품 조회 요청 시" - {
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


                "정상적으로 상품 코드를 응답" - {
                    val mockUUID = UUID.randomUUID()
                    Mockito.mockStatic(UUID::class.java).use { mockedUuid ->
                        mockedUuid.`when`<Any> { UUID.randomUUID().toString() }.thenReturn(mockUUID)

                        val productCode = mockUUID.toString()
                        val status = fixture<Product.Status>()
                        val expectedEntity = Product(
                            productName = fixture<String>(),
                            productPrice = fixture<Long>(),
                            productOptionGroupList = productOptionGroupList
                        )
                        ReflectionTestUtils.setField(expectedEntity, "status", status)
                        ReflectionTestUtils.setField(expectedEntity, "productCode", productCode)

                        Mockito.`when`(productRepository.findProductByProductCode(productCode)).thenReturn(expectedEntity)

                        val res = productReader.getProductByCode(productCode)
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
                        val expectedEntity = Product(
                            productName = fixture<String>(),
                            productPrice = fixture<Long>(),
                            productOptionGroupList = null
                        )
                        ReflectionTestUtils.setField(expectedEntity, "status", status)
                        ReflectionTestUtils.setField(expectedEntity, "productCode", productCode)

                        Mockito.`when`(productRepository.findProductByProductCode(productCode)).thenReturn(expectedEntity)

                        val res = productReader.getProductByCode(productCode)
                        Assertions.assertThat(res).isEqualTo(expectedEntity)
                    }
                }
            }

            "존재하지 않는 상품 코드일 때" - {
                "EntityNotFoundException 에러 응답" - {
                    val mockUUID = UUID.randomUUID()
                    Mockito.mockStatic(UUID::class.java).use { mockedUuid ->
                        mockedUuid.`when`<Any> { UUID.randomUUID().toString() }.thenReturn(mockUUID)

                        val productCode = mockUUID.toString()
                        Mockito.`when`(productRepository.findProductByProductCode(productCode)).thenReturn(null)

                        val res = assertThrows(EntityNotFoundException::class.java) {
                            productReader.getProductByCode(productCode)
                        }

                        Assertions.assertThat(res.localizedMessage).isEqualTo("존재하지 않는 상품입니다.")
                    }
                }
            }

        }

        "ProductOptionGroupInfoList 요청 시 " - {
            "productOptionGroupList 가 null 이면" - {
                "NullPointerException - 존재하지 않는 옵션그룹 입니다. 응답" - {
                    val product = Product(
                        productName = fixture<String>(),
                        productPrice = fixture<Long>(),
                        productOptionGroupList = null
                    )
                    val res = assertThrows(NullPointerException::class.java) {
                        productReader.getProductOptionGroupInfoList(product)
                    }
                    Assertions.assertThat(res.message).isEqualTo("존재하지 않는 옵션그룹 입니다.")
                }
            }

            "productOptionGroupList 가 null 이 아니면" - {

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

                "ProductOptionGroupInfo 리스트 응답" - {

                    val mockUUID = UUID.randomUUID()
                    Mockito.mockStatic(UUID::class.java).use { mockedUuid ->
                        mockedUuid.`when`<Any> { UUID.randomUUID().toString() }.thenReturn(mockUUID)

                        val productName = fixture<String>()
                        val productPrice = fixture<Long>()
                        val productCode = mockUUID.toString()
                        val status = fixture<Product.Status>()
                        val entity = Product(
                            productName = productName,
                            productPrice = productPrice,
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

                        val expectedProductOptionGroupInfoList = listOf(granolaProductOptionGroupInfo, honeyProductOptionGroupInfo)

                        val res = productReader.getProductOptionGroupInfoList(entity)
                        Assertions.assertThat(res).isEqualTo(expectedProductOptionGroupInfoList)
                    }

                }
            }
        }

    }
}
