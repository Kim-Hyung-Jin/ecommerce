package com.example.ecommerce.interfaces.product

import com.appmattus.kotlinfixture.kotlinFixture
import com.example.ecommerce.application.ProductFacade
import com.example.ecommerce.application.ProductResult
import com.example.ecommerce.domain.product.Product
import com.example.ecommerce.domain.product.ProductCommand
import com.example.ecommerce.domain.product.ProductCriteria
import com.example.ecommerce.domain.product.ProductInfo
import com.example.ecommerce.domain.product.option.ProductOption
import com.example.ecommerce.domain.product.optiongroup.ProductOptionGroup
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.FreeSpec
import io.kotest.spring.SpringListener
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*
import javax.persistence.EntityNotFoundException

@WebMvcTest(
    ObjectMapper::class,
    ProductController::class,
    ProductRequestMapperImpl::class
)
class ProductControllerTest : FreeSpec() {
    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    private val fixture = kotlinFixture()

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var productController: ProductController

    @MockBean
    private lateinit var productFacade: ProductFacade

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

                val productOptionGroupList = listOf(granolaProductOptionGroup, honeyProductOptionGroup)

                val request = ProductRequest.RegisterProduct(
                    productName = "그릭요거트 500g",
                    productPrice = 20000,
                    productOptionGroupList = productOptionGroupList
                )

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

                val expectedProductOptionGroupInfoList =
                    listOf(granolaProductOptionGroupInfo, honeyProductOptionGroupInfo)


                val productCode = UUID.randomUUID().toString()
                val expectedInfo = ProductInfo.ProductMain(
                    productName = request.productName,
                    productPrice = request.productPrice,
                    productCode = productCode,
                    status = Product.Status.PREPARE,
                    productOptionGroupList = expectedProductOptionGroupInfoList
                )

                val expectedResult = ProductResult.ProductMain(
                    productInfo = expectedInfo
                )

                val expectedResponse = ProductResponse.RegisterProduct(
                    productCode = expectedResult.productInfo.productCode
                )

                val command = ProductCommand.RegisterProduct(
                    productName = "그릭요거트 500g",
                    productPrice = 20000,
                    productOptionGroupList = productOptionGroupList
                )

                "정상적으로 상품 코드를 응답" - {
                    Mockito.`when`(productFacade.registerProduct(command)).thenReturn(expectedInfo.productCode)
                    val actual = mockMvc.perform(
                        post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request))
                    )
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andReturn()

                    val content = actual.response.contentAsString
                    val response = mapper.readValue<ApiResult<ProductResponse.RegisterProduct>>(content)

                    Assertions.assertThat(response.data).isEqualTo(expectedResponse)
                }
            }

            "추가 옵션 없을 때" - {
                val request = ProductRequest.RegisterProduct(
                    productName = "그릭요거트 500g",
                    productPrice = 20000,
                    productOptionGroupList = null
                )

                val productCode = UUID.randomUUID().toString()
                val expectedInfo = ProductInfo.ProductMain(
                    productName = request.productName,
                    productPrice = request.productPrice,
                    productCode = productCode,
                    status = Product.Status.PREPARE,
                    productOptionGroupList = null
                )

                val expectedResult = ProductResult.ProductMain(
                    productInfo = expectedInfo
                )

                val expectedResponse = ProductResponse.RegisterProduct(
                    productCode = expectedResult.productInfo.productCode
                )

                val command = ProductCommand.RegisterProduct(
                    productName = "그릭요거트 500g",
                    productPrice = 20000,
                    productOptionGroupList = null
                )

                "정상적으로 상품 코드를 응답" - {
                    Mockito.`when`(productFacade.registerProduct(command)).thenReturn(expectedInfo.productCode)
                    val actual = mockMvc.perform(
                        post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request))
                    )
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andReturn()

                    val content = actual.response.contentAsString
                    val response = mapper.readValue<ApiResult<ProductResponse.RegisterProduct>>(content)

                    Assertions.assertThat(response.data).isEqualTo(expectedResponse)
                }

            }
        }

        "상품 조회 시 " - {
            "등록된 상품이 없을 때" - {

                val productCode = UUID.randomUUID().toString()
                val criteria = ProductCriteria.GetProduct(
                    productCode = productCode
                )
                "정상적으로 상품 코드를 응답" - {
                    Mockito.`when`(productFacade.getProduct(criteria)).thenThrow(EntityNotFoundException("찾을 수 없는 엔티티"))
                    val actual = mockMvc.perform(
                        get("/api/v1/products/$productCode")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError)
                        .andExpect { result ->
                            assertTrue(result.resolvedException is EntityNotFoundException)
                        }
                        .andReturn()

                    val content = actual.response.contentAsString
                    val response = mapper.readValue<ApiResult<Unit>>(content)
                    Assertions.assertThat(response)
                }
            }

            "추가 옵션이 없는 상품이 존재 할 때" - {

                val productCode = UUID.randomUUID().toString()
                val criteria = ProductCriteria.GetProduct(
                    productCode = productCode
                )
                val expectedInfo = ProductInfo.ProductMain(
                    productName = fixture<String>(),
                    productPrice = fixture<Long>(),
                    productCode = productCode,
                    status = fixture<Product.Status>(),
                    productOptionGroupList = null
                )

                val expectedResult = ProductResult.ProductMain(
                    productInfo = expectedInfo
                )

                val expectedResponse = ProductResponse.RegisterProduct(
                    productCode = expectedResult.productInfo.productCode
                )

                "정상적으로 상품 코드를 응답" - {
                    Mockito.`when`(productFacade.getProduct(criteria)).thenReturn(expectedResult)
                    val actual = mockMvc.perform(
                        get("/api/v1/products/$productCode")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andReturn()

                    val content = actual.response.contentAsString
                    val response = mapper.readValue<ApiResult<ProductResponse.RegisterProduct>>(content)

                    Assertions.assertThat(response.data).isEqualTo(expectedResponse)
                }
            }

            "추가 옵션을 등록한 상품이 존재 할 때" - {



                val granolaProductOption = ProductInfo.ProductOptionInfo(
                    productOptionName = "그래놀라 500g",
                    ordering = 1,
                    productOptionPrice = 5000
                )

                val granolaProductOption2 = ProductInfo.ProductOptionInfo(
                    productOptionName = "그래놀라 1kg",
                    ordering = 2,
                    productOptionPrice = 10000
                )

                val granolaProductOption3 = ProductInfo.ProductOptionInfo(
                    productOptionName = "그래놀라 3kg",
                    ordering = 3,
                    productOptionPrice = 2000
                )

                val granolaProductOptionGroup = ProductInfo.ProductOptionGroupInfo(
                    productOptionGroupName = "그래놀라 추가",
                    ordering = 1,
                    productOptionList = listOf(granolaProductOption, granolaProductOption2, granolaProductOption3)
                )

                val honeyProductOption = ProductInfo.ProductOptionInfo(
                    productOptionName = "꿀 500g",
                    ordering = 1,
                    productOptionPrice = 3000
                )

                val honeyProductOption2 = ProductInfo.ProductOptionInfo(
                    productOptionName = "꿀 1kg",
                    ordering = 1,
                    productOptionPrice = 5500
                )

                val honeyProductOption3 = ProductInfo.ProductOptionInfo(
                    productOptionName = "꿀 3kg",
                    ordering = 3,
                    productOptionPrice = 145000
                )

                val honeyProductOptionGroup = ProductInfo.ProductOptionGroupInfo(
                    productOptionGroupName = "꿀 추가",
                    ordering = 2,
                    productOptionList = listOf(honeyProductOption, honeyProductOption2, honeyProductOption3)
                )

                val productOptionGroupList = listOf(granolaProductOptionGroup, honeyProductOptionGroup)


                val productCode = UUID.randomUUID().toString()
                val criteria = ProductCriteria.GetProduct(
                    productCode = productCode
                )
                val expectedInfo = ProductInfo.ProductMain(
                    productName = fixture<String>(),
                    productPrice = fixture<Long>(),
                    productCode = productCode,
                    status = fixture<Product.Status>(),
                    productOptionGroupList = productOptionGroupList
                )

                val expectedResult = ProductResult.ProductMain(
                    productInfo = expectedInfo
                )

                val expectedResponse = ProductResponse.RegisterProduct(
                    productCode = expectedResult.productInfo.productCode
                )

                "정상적으로 상품 코드를 응답" - {
                    Mockito.`when`(productFacade.getProduct(criteria)).thenReturn(expectedResult)
                    val actual = mockMvc.perform(
                        get("/api/v1/products/$productCode")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andReturn()

                    val content = actual.response.contentAsString
                    val response = mapper.readValue<ApiResult<ProductResponse.RegisterProduct>>(content)

                    Assertions.assertThat(response.data).isEqualTo(expectedResponse)
                }
            }
        }


    }

}
