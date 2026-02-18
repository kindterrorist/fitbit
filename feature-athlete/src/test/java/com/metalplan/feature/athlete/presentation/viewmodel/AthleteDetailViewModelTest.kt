package com.metalplan.feature.athlete.presentation.viewmodel

import com.metalplan.domain.common.DomainResult
import com.metalplan.feature.athlete.domain.model.Athlete
import com.metalplan.feature.athlete.domain.usecase.CreateAthleteUseCase
import com.metalplan.feature.athlete.domain.usecase.GetAthleteByIdUseCase
import com.metalplan.feature.athlete.domain.usecase.UpdateAthleteUseCase
import io.mockk.coEvery
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

    @Before
    fun setUp() {
        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @Test
    fun `save surfaces validation error`() = runTest {
        val viewModel = AthleteDetailViewModel(createUseCase, updateUseCase, getUseCase)
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
        val viewModel = AthleteDetailViewModel(createUseCase, updateUseCase, getUseCase)
        coEvery { getUseCase.invoke("1") } returns Athlete(
            id = "1",
            fullName = "Loaded",
            email = "loaded@test.com",
            sport = "Run",
            active = true,
            createdAt = 1,
            updatedAt = 1
        )

        viewModel.loadAthlete("1")
        advanceUntilIdle()

        assertEquals("Loaded", viewModel.formState.value.fullName)
    }
}
