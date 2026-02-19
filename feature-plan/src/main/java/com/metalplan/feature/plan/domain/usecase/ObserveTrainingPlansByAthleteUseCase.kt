package com.metalplan.feature.plan.domain.usecase

import com.metalplan.feature.plan.domain.model.TrainingPlan
import com.metalplan.feature.plan.domain.repository.TrainingPlanRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTrainingPlansByAthleteUseCase @Inject constructor(
    private val repository: TrainingPlanRepository
) {
    operator fun invoke(athleteId: String, activeOnly: Boolean): Flow<List<TrainingPlan>> {
        return repository.observeByAthlete(athleteId, activeOnly)
    }
}
