package com.metalplan.feature.athlete.data.repository

import com.metalplan.feature.athlete.data.local.dao.AthleteDao
import com.metalplan.feature.athlete.data.local.entity.AthleteEntity
import com.metalplan.feature.athlete.domain.model.Athlete
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AthleteRepositoryImplTest {

    private val fakeDao = FakeAthleteDao()
    private val repository = AthleteRepositoryImpl(fakeDao)

    @Test
    fun `repository maps dao models`() = runTest {
        val athlete = Athlete(
            id = "id",
            fullName = "Name",
            email = "email@test.com",
            sport = "Cycling",
            active = true,
            createdAt = 1L,
            updatedAt = 1L
        )

        repository.upsertAthlete(athlete)
        val result = repository.observeAthletes(null, false).first()

        assertEquals("Name", result.first().fullName)
    }
}

private class FakeAthleteDao : AthleteDao {
    private val cache = linkedMapOf<String, AthleteEntity>()
    private val flow = MutableStateFlow<List<AthleteEntity>>(emptyList())

    override fun observeAthletes(query: String?, activeOnly: Boolean): Flow<List<AthleteEntity>> = flow

    override suspend fun getAthleteById(id: String): AthleteEntity? = cache[id]

    override suspend fun upsert(athlete: AthleteEntity) {
        cache[athlete.id] = athlete
        flow.value = cache.values.toList()
    }

    override suspend fun deleteById(id: String) {
        cache.remove(id)
        flow.value = cache.values.toList()
    }
}
