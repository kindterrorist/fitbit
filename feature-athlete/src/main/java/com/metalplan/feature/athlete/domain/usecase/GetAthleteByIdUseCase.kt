package com.metalplan.feature.athlete.domain.usecase

import com.metalplan.feature.athlete.domain.model.Athlete
import com.metalplan.feature.athlete.domain.repository.AthleteRepository
import javax.inject.Inject

class GetAthleteByIdUseCase @Inject constructor(
    private val repository: AthleteRepository
) {
    suspend operator fun invoke(id: String): Athlete? = repository.getAthleteById(id)
}
