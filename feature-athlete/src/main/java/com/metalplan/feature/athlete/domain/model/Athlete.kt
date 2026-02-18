package com.metalplan.feature.athlete.domain.model

import java.util.UUID

data class Athlete(
    val id: String = UUID.randomUUID().toString(),
    val fullName: String,
    val email: String,
    val sport: String,
    val active: Boolean,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun validate(): ValidationResult {
        return when {
            fullName.isBlank() -> ValidationResult.Invalid("Name is required")
            !email.contains("@") -> ValidationResult.Invalid("Valid email is required")
            sport.isBlank() -> ValidationResult.Invalid("Sport is required")
            else -> ValidationResult.Valid
        }
    }
}

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val reason: String) : ValidationResult
}
