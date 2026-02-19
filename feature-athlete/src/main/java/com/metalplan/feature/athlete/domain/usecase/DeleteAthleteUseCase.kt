package com.metalplan.feature.athlete.domain.usecase

import com.metalplan.feature.athlete.domain.repository.AthleteRepository
import javax.inject.Inject

class DeleteAthleteUseCase @Inject constructor(
    private val repository: AthleteRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteAthlete(id)
    }
}
