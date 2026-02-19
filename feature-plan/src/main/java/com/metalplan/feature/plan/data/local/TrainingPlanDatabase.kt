package com.metalplan.feature.plan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.metalplan.feature.plan.data.local.dao.TrainingPlanDao
import com.metalplan.feature.plan.data.local.entity.ExerciseEntity
import com.metalplan.feature.plan.data.local.entity.TrainingPlanEntity
import com.metalplan.feature.plan.data.local.entity.WorkoutEntity

@Database(
    entities = [TrainingPlanEntity::class, WorkoutEntity::class, ExerciseEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TrainingPlanDatabase : RoomDatabase() {
    abstract fun trainingPlanDao(): TrainingPlanDao
}
