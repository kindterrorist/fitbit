package com.metalplan.feature.plan.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.metalplan.domain.common.DomainResult
import com.metalplan.domain.common.TimeProvider
import com.metalplan.feature.plan.domain.usecase.CreateTrainingPlanUseCase
import com.metalplan.feature.plan.domain.usecase.GetTrainingPlanByIdUseCase
import com.metalplan.feature.plan.domain.usecase.UpdateTrainingPlanUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
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
class TrainingPlanDetailViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val createUseCase = mockk<CreateTrainingPlanUseCase>()
    private val updateUseCase = mockk<UpdateTrainingPlanUseCase>()
    private val getUseCase = mockk<GetTrainingPlanByIdUseCase>()
    private val timeProvider = mockk<TimeProvider>()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        every { timeProvider.nowMillis() } returns 999L
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save returns validation message from use case`() = runTest {
        val viewModel = TrainingPlanDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("athleteId" to "athlete-1")),
            createTrainingPlanUseCase = createUseCase,
            updateTrainingPlanUseCase = updateUseCase,
            getTrainingPlanByIdUseCase = getUseCase,
            timeProvider = timeProvider
        )
        coEvery { createUseCase.invoke(any()) } returns DomainResult.Error("invalid")

        viewModel.save {}
        advanceUntilIdle()

        assertEquals("invalid", viewModel.formState.value.errorMessage)
    }
}
