package com.metalplan.feature.plan.data.repository

import com.metalplan.feature.plan.data.local.TrainingPlanWithWorkouts
import com.metalplan.feature.plan.data.local.WorkoutWithExercises
import com.metalplan.feature.plan.data.local.dao.TrainingPlanDao
import com.metalplan.feature.plan.data.local.entity.ExerciseEntity
import com.metalplan.feature.plan.data.local.entity.TrainingPlanEntity
import com.metalplan.feature.plan.data.local.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TrainingPlanRepositoryImplTest {
    @Test
    fun `observe maps nested relation into domain`() = runTest {
        val dao = FakeTrainingPlanDao()
        val repository = TrainingPlanRepositoryImpl(dao)

        val plans = repository.observeByAthlete("athlete-1", true).first()

        assertEquals("Plan", plans.first().title)
        assertEquals("Squat", plans.first().workouts.first().exercises.first().name)
    }
}

private class FakeTrainingPlanDao : TrainingPlanDao {
    private val flow = MutableStateFlow(
        listOf(
            TrainingPlanWithWorkouts(
                plan = TrainingPlanEntity("plan-1", "athlete-1", "Plan", "Goal", 1, 2, true, 1, 1),
                workouts = listOf(
                    WorkoutWithExercises(
                        workout = WorkoutEntity("workout-1", "plan-1", "W1", 1, 0),
                        exercises = listOf(
                            ExerciseEntity("exercise-1", "workout-1", "Squat", 5, 5, 120, "", 0)
                        )
                    )
                )
            )
        )
    )

    override fun observeByAthlete(athleteId: String, activeOnly: Boolean): Flow<List<TrainingPlanWithWorkouts>> = flow

    override suspend fun getById(planId: String): TrainingPlanWithWorkouts? = flow.value.firstOrNull()
    override suspend fun upsertPlan(plan: TrainingPlanEntity) = Unit
    override suspend fun upsertWorkouts(workouts: List<WorkoutEntity>) = Unit
    override suspend fun upsertExercises(exercises: List<ExerciseEntity>) = Unit
    override suspend fun deleteWorkoutsByPlanId(planId: String) = Unit
    override suspend fun deletePlan(planId: String) = Unit
}
