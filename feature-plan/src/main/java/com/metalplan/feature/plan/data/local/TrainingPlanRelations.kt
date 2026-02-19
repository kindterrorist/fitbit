package com.metalplan.feature.plan.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.metalplan.feature.plan.data.local.entity.ExerciseEntity
import com.metalplan.feature.plan.data.local.entity.TrainingPlanEntity
import com.metalplan.feature.plan.data.local.entity.WorkoutEntity

data class WorkoutWithExercises(
    @Embedded val workout: WorkoutEntity,
    @Relation(parentColumn = "id", entityColumn = "workoutId")
    val exercises: List<ExerciseEntity>
)

data class TrainingPlanWithWorkouts(
    @Embedded val plan: TrainingPlanEntity,
    @Relation(entity = WorkoutEntity::class, parentColumn = "id", entityColumn = "trainingPlanId")
    val workouts: List<WorkoutWithExercises>
)
