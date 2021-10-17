package com.example.ecommerce.interfaces.product

data class ApiResult<T>(
    val result: Boolean,
    val message: String,
    val errorCode: String?,
    val data: T?
) {
    companion object {
        fun <T> ok(message: String, data: T?): ApiResult<T> {
            return ApiResult(result = true, message = message, errorCode = null, data = data)
        }

        fun <T> error(message: String, errorCode: String): ApiResult<T> {
            return ApiResult(result = false, message = message, errorCode = errorCode, data = null)
        }
    }
}