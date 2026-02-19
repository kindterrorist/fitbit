package com.metalplan.feature.plan.domain.usecase

import com.metalplan.domain.common.DomainResult
import com.metalplan.feature.plan.domain.model.PlanValidationResult
import com.metalplan.feature.plan.domain.model.TrainingPlan
import com.metalplan.feature.plan.domain.repository.TrainingPlanRepository
import javax.inject.Inject

class UpdateTrainingPlanUseCase @Inject constructor(
    private val repository: TrainingPlanRepository
) {
    suspend operator fun invoke(plan: TrainingPlan): DomainResult<Unit> {
        return when (val validation = plan.validate()) {
            is PlanValidationResult.Invalid -> DomainResult.Error(validation.reason)
            PlanValidationResult.Valid -> {
                repository.upsert(plan)
                DomainResult.Success(Unit)
            }
        }
    }
}
