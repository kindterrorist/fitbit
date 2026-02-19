package com.metalplan.feature.plan.domain.model

import java.util.UUID

data class TrainingPlan(
    val id: String = UUID.randomUUID().toString(),
    val athleteId: String,
    val title: String,
    val goal: String,
    val startDateEpochDay: Long,
    val endDateEpochDay: Long,
    val isActive: Boolean,
    val workouts: List<Workout>,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun validate(): PlanValidationResult {
        return when {
            athleteId.isBlank() -> PlanValidationResult.Invalid("Athlete id is required")
            title.isBlank() -> PlanValidationResult.Invalid("Title is required")
            endDateEpochDay < startDateEpochDay -> PlanValidationResult.Invalid("Date range is invalid")
            workouts.any { it.exercises.isEmpty() } -> PlanValidationResult.Invalid("Workout must contain at least one exercise")
            else -> PlanValidationResult.Valid
        }
    }
}

data class Workout(
    val id: String = UUID.randomUUID().toString(),
    val trainingPlanId: String,
    val name: String,
    val dayOfWeek: Int,
    val position: Int,
    val exercises: List<Exercise>
)

data class Exercise(
    val id: String = UUID.randomUUID().toString(),
    val workoutId: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val restSeconds: Int,
    val notes: String,
    val position: Int
)

sealed interface PlanValidationResult {
    data object Valid : PlanValidationResult
    data class Invalid(val reason: String) : PlanValidationResult
}
