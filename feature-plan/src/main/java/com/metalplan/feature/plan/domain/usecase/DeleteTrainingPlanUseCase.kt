package com.metalplan.feature.plan.domain.usecase

import com.metalplan.feature.plan.domain.repository.TrainingPlanRepository
import javax.inject.Inject

class DeleteTrainingPlanUseCase @Inject constructor(
    private val repository: TrainingPlanRepository
) {
    suspend operator fun invoke(planId: String) = repository.delete(planId)
}
