package com.metalplan.feature.athlete.data.mapper

import com.metalplan.feature.athlete.data.local.entity.AthleteEntity
import com.metalplan.feature.athlete.domain.model.Athlete

fun AthleteEntity.toDomain(): Athlete = Athlete(
    id = id,
    fullName = fullName,
    email = email,
    sport = sport,
    active = active,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Athlete.toEntity(): AthleteEntity = AthleteEntity(
    id = id,
    fullName = fullName,
    email = email,
    sport = sport,
    active = active,
    createdAt = createdAt,
    updatedAt = updatedAt
)
