package com.metalplan.feature.athlete.domain.usecase

import com.metalplan.feature.athlete.domain.model.Athlete
import com.metalplan.feature.athlete.domain.repository.AthleteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAthletesUseCase @Inject constructor(
    private val repository: AthleteRepository
) {
    operator fun invoke(query: String?, activeOnly: Boolean): Flow<List<Athlete>> {
        return repository.observeAthletes(query, activeOnly)
    }
}
