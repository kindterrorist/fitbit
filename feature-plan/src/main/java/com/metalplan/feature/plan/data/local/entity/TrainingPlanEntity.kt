package com.metalplan.feature.plan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_plans")
data class TrainingPlanEntity(
    @PrimaryKey val id: String,
    val athleteId: String,
    val title: String,
    val goal: String,
    val startDateEpochDay: Long,
    val endDateEpochDay: Long,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
