package com.metalplan.feature.athlete.presentation.viewmodel

import com.metalplan.feature.athlete.domain.model.Athlete
import com.metalplan.feature.athlete.domain.repository.AthleteRepository
import com.metalplan.feature.athlete.domain.usecase.DeleteAthleteUseCase
import com.metalplan.feature.athlete.domain.usecase.ObserveAthletesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AthleteListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state reflects search and active filters`() = runTest {
        val repository = FilteringFakeAthleteRepository(
            listOf(
                athlete("1", "Alex Runner", true),
                athlete("2", "Sam Lifter", false)
            )
        )
        val viewModel = AthleteListViewModel(
            observeAthletesUseCase = ObserveAthletesUseCase(repository),
            deleteAthleteUseCase = DeleteAthleteUseCase(repository)
        )

        viewModel.onSearchChanged("Alex")
        viewModel.onActiveFilterChanged(true)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Alex", state.query)
        assertEquals(true, state.activeOnly)
        assertEquals(1, state.athletes.size)
        assertEquals("Alex Runner", state.athletes.first().fullName)
    }

    private fun athlete(id: String, name: String, active: Boolean) = Athlete(
        id = id,
        fullName = name,
        email = "$id@test.com",
        sport = "Running",
        active = active,
        createdAt = 1L,
        updatedAt = 1L
    )
}

private class FilteringFakeAthleteRepository(initial: List<Athlete>) : AthleteRepository {
    private val store = LinkedHashMap<String, Athlete>().apply {
        initial.forEach { put(it.id, it) }
    }
    private val source = MutableStateFlow(store.values.toList())

    override fun observeAthletes(query: String?, activeOnly: Boolean): Flow<List<Athlete>> {
        return source.map { list ->
            list.filter { athlete ->
                val queryMatch = query.isNullOrBlank() ||
                    athlete.fullName.contains(query, ignoreCase = true) ||
                    athlete.email.contains(query, ignoreCase = true)
                val activeMatch = !activeOnly || athlete.active
                queryMatch && activeMatch
            }
        }
    }

    override suspend fun getAthleteById(id: String): Athlete? = store[id]

    override suspend fun upsertAthlete(athlete: Athlete) {
        store[athlete.id] = athlete
        source.value = store.values.toList()
    }

    override suspend fun deleteAthlete(id: String) {
        store.remove(id)
        source.value = store.values.toList()
    }
}
