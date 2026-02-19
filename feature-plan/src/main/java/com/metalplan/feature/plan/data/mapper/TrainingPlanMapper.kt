package com.metalplan.feature.plan.data.mapper

import com.metalplan.feature.plan.data.local.TrainingPlanWithWorkouts
import com.metalplan.feature.plan.data.local.entity.ExerciseEntity
import com.metalplan.feature.plan.data.local.entity.TrainingPlanEntity
import com.metalplan.feature.plan.data.local.entity.WorkoutEntity
import com.metalplan.feature.plan.domain.model.Exercise
import com.metalplan.feature.plan.domain.model.TrainingPlan
import com.metalplan.feature.plan.domain.model.Workout

fun TrainingPlanWithWorkouts.toDomain(): TrainingPlan = TrainingPlan(
    id = plan.id,
    athleteId = plan.athleteId,
    title = plan.title,
    goal = plan.goal,
    startDateEpochDay = plan.startDateEpochDay,
    endDateEpochDay = plan.endDateEpochDay,
    isActive = plan.isActive,
    createdAt = plan.createdAt,
    updatedAt = plan.updatedAt,
    workouts = workouts.sortedBy { it.workout.position }.map { workout ->
        Workout(
            id = workout.workout.id,
            trainingPlanId = workout.workout.trainingPlanId,
            name = workout.workout.name,
            dayOfWeek = workout.workout.dayOfWeek,
            position = workout.workout.position,
            exercises = workout.exercises.sortedBy { it.position }.map { exercise ->
                Exercise(
                    id = exercise.id,
                    workoutId = exercise.workoutId,
                    name = exercise.name,
                    sets = exercise.sets,
                    reps = exercise.reps,
                    restSeconds = exercise.restSeconds,
                    notes = exercise.notes,
                    position = exercise.position
                )
            }
        )
    }
)

fun TrainingPlan.toPlanEntity(): TrainingPlanEntity = TrainingPlanEntity(
    id = id,
    athleteId = athleteId,
    title = title,
    goal = goal,
    startDateEpochDay = startDateEpochDay,
    endDateEpochDay = endDateEpochDay,
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun TrainingPlan.toWorkoutEntities(): List<WorkoutEntity> = workouts.map { workout ->
    WorkoutEntity(
        id = workout.id,
        trainingPlanId = id,
        name = workout.name,
        dayOfWeek = workout.dayOfWeek,
        position = workout.position
    )
}

fun TrainingPlan.toExerciseEntities(): List<ExerciseEntity> = workouts.flatMap { workout ->
    workout.exercises.map { exercise ->
        ExerciseEntity(
            id = exercise.id,
            workoutId = workout.id,
            name = exercise.name,
            sets = exercise.sets,
            reps = exercise.reps,
            restSeconds = exercise.restSeconds,
            notes = exercise.notes,
            position = exercise.position
        )
    }
}
