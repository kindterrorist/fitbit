package com.metalplan.feature.plan.data.repository

import com.metalplan.feature.plan.data.local.dao.TrainingPlanDao
import com.metalplan.feature.plan.data.mapper.toDomain
import com.metalplan.feature.plan.data.mapper.toExerciseEntities
import com.metalplan.feature.plan.data.mapper.toPlanEntity
import com.metalplan.feature.plan.data.mapper.toWorkoutEntities
import com.metalplan.feature.plan.domain.model.TrainingPlan
import com.metalplan.feature.plan.domain.repository.TrainingPlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrainingPlanRepositoryImpl @Inject constructor(
    private val dao: TrainingPlanDao
) : TrainingPlanRepository {
    override fun observeByAthlete(athleteId: String, activeOnly: Boolean): Flow<List<TrainingPlan>> {
        return dao.observeByAthlete(athleteId, activeOnly).map { plans ->
            plans.map { it.toDomain() }
        }
    }

    override suspend fun getById(planId: String): TrainingPlan? = dao.getById(planId)?.toDomain()

    override suspend fun upsert(plan: TrainingPlan) {
        dao.upsertPlan(plan.toPlanEntity())
        dao.deleteWorkoutsByPlanId(plan.id)
        dao.upsertWorkouts(plan.toWorkoutEntities())
        dao.upsertExercises(plan.toExerciseEntities())
    }

    override suspend fun delete(planId: String) {
        dao.deletePlan(planId)
    }
}
