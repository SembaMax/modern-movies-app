package com.semba.modernmoviesapp.core.common

sealed class DataResponse<T>(
    val data: T? = null,
    val errorCode: ErrorCode? = null
) {
    class Success<T>(data: T) : DataResponse<T>(data = data, errorCode = null)
    class Failure<T>(errorCode: ErrorCode?) : DataResponse<T>(data = null, errorCode = errorCode)
}

enum class ErrorCode (val code: Int) {
    UNSPECIFIED(-1),
    NOT_FOUND(404),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    SERVER_ERROR(500),
    DATABASE_ERROR(1000);

    companion object {
        fun from(code: Int): ErrorCode = ErrorCode.values().first { it.code == code }
    }
}

fun ErrorCode.errorMessage(): String {
    return "Error"
}