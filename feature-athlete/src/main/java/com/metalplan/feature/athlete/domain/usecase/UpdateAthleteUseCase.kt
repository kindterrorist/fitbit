package com.metalplan.feature.athlete.domain.usecase

import com.metalplan.domain.common.DomainResult
import com.metalplan.feature.athlete.domain.model.Athlete
import com.metalplan.feature.athlete.domain.model.ValidationResult
import com.metalplan.feature.athlete.domain.repository.AthleteRepository
import javax.inject.Inject

class UpdateAthleteUseCase @Inject constructor(
    private val repository: AthleteRepository
) {
    suspend operator fun invoke(athlete: Athlete): DomainResult<Unit> {
        return when (val validation = athlete.validate()) {
            is ValidationResult.Invalid -> DomainResult.Error(validation.reason)
            ValidationResult.Valid -> {
                repository.upsertAthlete(athlete.copy(updatedAt = System.currentTimeMillis()))
                DomainResult.Success(Unit)
            }
        }
    }
}
