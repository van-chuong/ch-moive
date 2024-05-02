package com.example.chmovie.shared.utils

sealed class ResponseResult {
    data class Success(val message: String) : ResponseResult()
    data class Error(val error: String) : ResponseResult()
}