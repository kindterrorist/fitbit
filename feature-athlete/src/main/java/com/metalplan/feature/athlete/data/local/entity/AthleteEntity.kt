package com.metalplan.feature.athlete.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "athletes")
data class AthleteEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val email: String,
    val sport: String,
    val active: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
