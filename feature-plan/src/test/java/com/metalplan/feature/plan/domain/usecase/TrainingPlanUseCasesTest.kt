package com.metalplan.feature.plan.domain.usecase

import com.metalplan.domain.common.DomainResult
import com.metalplan.feature.plan.domain.model.Exercise
import com.metalplan.feature.plan.domain.model.TrainingPlan
import com.metalplan.feature.plan.domain.model.Workout
import com.metalplan.feature.plan.domain.repository.TrainingPlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TrainingPlanUseCasesTest {

    private val repository = FakeTrainingPlanRepository()
    private val createUseCase = CreateTrainingPlanUseCase(repository)
    private val updateUseCase = UpdateTrainingPlanUseCase(repository)
    private val deleteUseCase = DeleteTrainingPlanUseCase(repository)
    private val getUseCase = GetTrainingPlanByIdUseCase(repository)
    private val observeUseCase = ObserveTrainingPlansByAthleteUseCase(repository)

    @Test
    fun `create fails when title is empty`() = runTest {
        val result = createUseCase(samplePlan(title = ""))
        assertTrue(result is DomainResult.Error)
    }

    @Test
    fun `create and get returns persisted plan`() = runTest {
        val plan = samplePlan()
        createUseCase(plan)
        assertEquals(plan.id, getUseCase(plan.id)?.id)
    }

    @Test
    fun `update modifies title`() = runTest {
        val plan = samplePlan()
        createUseCase(plan)
        updateUseCase(plan.copy(title = "Updated"))
        assertEquals("Updated", getUseCase(plan.id)?.title)
    }

    @Test
    fun `delete removes plan`() = runTest {
        val plan = samplePlan()
        createUseCase(plan)
        deleteUseCase(plan.id)
        assertEquals(null, getUseCase(plan.id))
    }

    @Test
    fun `observe by athlete returns matching plans`() = runTest {
        val plan = samplePlan()
        createUseCase(plan)
        val observed = observeUseCase("athlete-1", true).first()
        assertEquals(1, observed.size)
    }

    private fun samplePlan(title: String = "Base Plan"): TrainingPlan {
        val workoutId = "workout-1"
        return TrainingPlan(
            id = "plan-1",
            athleteId = "athlete-1",
            title = title,
            goal = "Strength",
            startDateEpochDay = 100,
            endDateEpochDay = 120,
            isActive = true,
            workouts = listOf(
                Workout(
                    id = workoutId,
                    trainingPlanId = "plan-1",
                    name = "Day 1",
                    dayOfWeek = 1,
                    position = 0,
                    exercises = listOf(
                        Exercise(
                            id = "exercise-1",
                            workoutId = workoutId,
                            name = "Squat",
                            sets = 5,
                            reps = 5,
                            restSeconds = 120,
                            notes = "Heavy",
                            position = 0
                        )
                    )
                )
            ),
            createdAt = 10,
            updatedAt = 10
        )
    }
}

private class FakeTrainingPlanRepository : TrainingPlanRepository {
    private val store = linkedMapOf<String, TrainingPlan>()
    private val flow = MutableStateFlow<List<TrainingPlan>>(emptyList())

    override fun observeByAthlete(athleteId: String, activeOnly: Boolean): Flow<List<TrainingPlan>> =
        MutableStateFlow(flow.value.filter { it.athleteId == athleteId && (!activeOnly || it.isActive) })

    override suspend fun getById(planId: String): TrainingPlan? = store[planId]

    override suspend fun upsert(plan: TrainingPlan) {
        store[plan.id] = plan
        flow.value = store.values.toList()
    }

    override suspend fun delete(planId: String) {
        store.remove(planId)
        flow.value = store.values.toList()
    }
}
