package com.metalplan.feature.athlete.domain.usecase

import com.metalplan.domain.common.DomainResult
import com.metalplan.feature.athlete.domain.model.Athlete
import com.metalplan.feature.athlete.domain.repository.AthleteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AthleteUseCasesTest {

    private val fakeRepository = FakeAthleteRepository()
    private val createUseCase = CreateAthleteUseCase(fakeRepository)
    private val updateUseCase = UpdateAthleteUseCase(fakeRepository)
    private val deleteUseCase = DeleteAthleteUseCase(fakeRepository)
    private val getUseCase = GetAthleteByIdUseCase(fakeRepository)
    private val observeUseCase = ObserveAthletesUseCase(fakeRepository)

    @Test
    fun `create athlete returns error for invalid data`() = runTest {
        val result = createUseCase(sampleAthlete(email = "invalid"))
        assertTrue(result is DomainResult.Error)
    }

    @Test
    fun `create athlete saves valid athlete`() = runTest {
        val athlete = sampleAthlete()
        createUseCase(athlete)
        assertEquals(athlete.id, getUseCase(athlete.id)?.id)
    }

    @Test
    fun `update athlete persists modifications`() = runTest {
        val athlete = sampleAthlete()
        createUseCase(athlete)
        updateUseCase(athlete.copy(fullName = "Updated"))
        assertEquals("Updated", getUseCase(athlete.id)?.fullName)
    }

    @Test
    fun `delete athlete removes entity`() = runTest {
        val athlete = sampleAthlete()
        createUseCase(athlete)
        deleteUseCase(athlete.id)
        assertEquals(null, getUseCase(athlete.id))
    }

    @Test
    fun `observe athletes emits list`() = runTest {
        val athlete = sampleAthlete()
        createUseCase(athlete)
        val list = observeUseCase(null, false).first()
        assertTrue(list.isNotEmpty())
    }

    private fun sampleAthlete(email: String = "athlete@metalplan.com") = Athlete(
        id = "a1",
        fullName = "Test Athlete",
        email = email,
        sport = "Running",
        active = true,
        createdAt = 1L,
        updatedAt = 1L
    )
}

private class FakeAthleteRepository : AthleteRepository {
    private val athletes = LinkedHashMap<String, Athlete>()
    private val flow = MutableStateFlow<List<Athlete>>(emptyList())

    override fun observeAthletes(query: String?, activeOnly: Boolean): Flow<List<Athlete>> = flow

    override suspend fun getAthleteById(id: String): Athlete? = athletes[id]

    override suspend fun upsertAthlete(athlete: Athlete) {
        athletes[athlete.id] = athlete
        flow.value = athletes.values.toList()
    }

    override suspend fun deleteAthlete(id: String) {
        athletes.remove(id)
        flow.value = athletes.values.toList()
    }
}
