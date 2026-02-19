package com.metalplan.feature.athlete.domain.repository

import com.metalplan.feature.athlete.domain.model.Athlete
import kotlinx.coroutines.flow.Flow

interface AthleteRepository {
    fun observeAthletes(query: String?, activeOnly: Boolean): Flow<List<Athlete>>
    suspend fun getAthleteById(id: String): Athlete?
    suspend fun upsertAthlete(athlete: Athlete)
    suspend fun deleteAthlete(id: String)
}
