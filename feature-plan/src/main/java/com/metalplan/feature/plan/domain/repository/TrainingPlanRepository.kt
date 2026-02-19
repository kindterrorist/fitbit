package com.metalplan.feature.plan.domain.repository

import com.metalplan.feature.plan.domain.model.TrainingPlan
import kotlinx.coroutines.flow.Flow

interface TrainingPlanRepository {
    fun observeByAthlete(athleteId: String, activeOnly: Boolean): Flow<List<TrainingPlan>>
    suspend fun getById(planId: String): TrainingPlan?
    suspend fun upsert(plan: TrainingPlan)
    suspend fun delete(planId: String)
}
