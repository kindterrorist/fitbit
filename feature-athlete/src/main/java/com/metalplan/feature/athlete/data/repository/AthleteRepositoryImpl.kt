package com.metalplan.feature.athlete.data.repository

import com.metalplan.feature.athlete.data.local.dao.AthleteDao
import com.metalplan.feature.athlete.data.mapper.toDomain
import com.metalplan.feature.athlete.data.mapper.toEntity
import com.metalplan.feature.athlete.domain.model.Athlete
import com.metalplan.feature.athlete.domain.repository.AthleteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AthleteRepositoryImpl @Inject constructor(
    private val athleteDao: AthleteDao
) : AthleteRepository {
    override fun observeAthletes(query: String?, activeOnly: Boolean): Flow<List<Athlete>> {
        return athleteDao.observeAthletes(query, activeOnly).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getAthleteById(id: String): Athlete? {
        return athleteDao.getAthleteById(id)?.toDomain()
    }

    override suspend fun upsertAthlete(athlete: Athlete) {
        athleteDao.upsert(athlete.toEntity())
    }

    override suspend fun deleteAthlete(id: String) {
        athleteDao.deleteById(id)
    }
}
