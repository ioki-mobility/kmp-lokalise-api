package com.ioki.lokalise.api

import com.ioki.lokalise.api.models.Error

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Failure(val error: Error) : Result<Nothing>
}