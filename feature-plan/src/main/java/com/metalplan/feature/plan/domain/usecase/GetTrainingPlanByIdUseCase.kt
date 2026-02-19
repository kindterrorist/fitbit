package com.metalplan.feature.plan.domain.usecase

import com.metalplan.feature.plan.domain.model.TrainingPlan
import com.metalplan.feature.plan.domain.repository.TrainingPlanRepository
import javax.inject.Inject

class GetTrainingPlanByIdUseCase @Inject constructor(
    private val repository: TrainingPlanRepository
) {
    suspend operator fun invoke(planId: String): TrainingPlan? = repository.getById(planId)
}
