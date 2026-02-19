package com.metalplan.feature.athlete.presentation.viewmodel

import com.metalplan.domain.common.DomainResult
import com.metalplan.domain.common.TimeProvider
import com.metalplan.feature.athlete.domain.model.Athlete
import com.metalplan.feature.athlete.domain.usecase.CreateAthleteUseCase
import com.metalplan.feature.athlete.domain.usecase.GetAthleteByIdUseCase
import com.metalplan.feature.athlete.domain.usecase.UpdateAthleteUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AthleteDetailViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val createUseCase = mockk<CreateAthleteUseCase>()
    private val updateUseCase = mockk<UpdateAthleteUseCase>()
    private val getUseCase = mockk<GetAthleteByIdUseCase>()
    private val timeProvider = mockk<TimeProvider>()

    @Before
    fun setUp() {
        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
        every { timeProvider.nowMillis() } returns 1_000L
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @Test
    fun `save surfaces validation error`() = runTest {
        val viewModel = AthleteDetailViewModel(createUseCase, updateUseCase, getUseCase, timeProvider)
        coEvery { createUseCase.invoke(any()) } returns DomainResult.Error("error")

        viewModel.onNameChanged("A")
        viewModel.onEmailChanged("invalid")
        viewModel.onSportChanged("Run")
        viewModel.save {}
        advanceUntilIdle()

        assertEquals("error", viewModel.formState.value.errorMessage)
    }

    @Test
    fun `load athlete populates form`() = runTest {
        val viewModel = AthleteDetailViewModel(createUseCase, updateUseCase, getUseCase, timeProvider)
        coEvery { getUseCase.invoke("1") } returns Athlete(
            id = "1",
            fullName = "Loaded",
            email = "loaded@test.com",
            sport = "Run",
            active = true,
            createdAt = 10,
            updatedAt = 12
        )

        viewModel.loadAthlete("1")
        advanceUntilIdle()

        assertEquals("Loaded", viewModel.formState.value.fullName)
        assertEquals(10, viewModel.formState.value.createdAt)
    }
}
