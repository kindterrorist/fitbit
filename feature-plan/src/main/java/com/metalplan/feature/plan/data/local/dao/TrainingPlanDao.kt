package com.metalplan.feature.plan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.metalplan.feature.plan.data.local.TrainingPlanWithWorkouts
import com.metalplan.feature.plan.data.local.entity.ExerciseEntity
import com.metalplan.feature.plan.data.local.entity.TrainingPlanEntity
import com.metalplan.feature.plan.data.local.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingPlanDao {
    @Transaction
    @Query(
        """
        SELECT * FROM training_plans
        WHERE athleteId = :athleteId
          AND (:activeOnly = 0 OR isActive = 1)
        ORDER BY updatedAt DESC
        """
    )
    fun observeByAthlete(athleteId: String, activeOnly: Boolean): Flow<List<TrainingPlanWithWorkouts>>

    @Transaction
    @Query("SELECT * FROM training_plans WHERE id = :planId LIMIT 1")
    suspend fun getById(planId: String): TrainingPlanWithWorkouts?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlan(plan: TrainingPlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWorkouts(workouts: List<WorkoutEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExercises(exercises: List<ExerciseEntity>)

    @Query("DELETE FROM workouts WHERE trainingPlanId = :planId")
    suspend fun deleteWorkoutsByPlanId(planId: String)

    @Query("DELETE FROM training_plans WHERE id = :planId")
    suspend fun deletePlan(planId: String)
}
