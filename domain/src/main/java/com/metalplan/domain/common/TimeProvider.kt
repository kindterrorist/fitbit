package com.metalplan.domain.common

interface TimeProvider {
    fun nowMillis(): Long
}
