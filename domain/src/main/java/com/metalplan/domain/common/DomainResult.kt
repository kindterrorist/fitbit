package com.metalplan.domain.common

sealed interface DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>
    data class Error(val message: String, val throwable: Throwable? = null) : DomainResult<Nothing>
}
