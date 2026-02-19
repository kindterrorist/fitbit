package com.metalplan.feature.plan.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = TrainingPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["trainingPlanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("trainingPlanId")]
)
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val trainingPlanId: String,
    val name: String,
    val dayOfWeek: Int,
    val position: Int
)
