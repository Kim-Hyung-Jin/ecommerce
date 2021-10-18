package com.example.ecommerce.interfaces

import com.example.ecommerce.interfaces.product.ApiResult
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.persistence.EntityNotFoundException

@ControllerAdvice
@RestController
class ExceptionController {

//    private val logger by getLogger()

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNoBalanceException(e: EntityNotFoundException): ApiResult<Unit> {
        logger.info("EntityNotFoundException:", e)

        return ApiResult.error(message = e.localizedMessage, errorCode = HttpStatus.BAD_REQUEST.value().toString())
    }

}