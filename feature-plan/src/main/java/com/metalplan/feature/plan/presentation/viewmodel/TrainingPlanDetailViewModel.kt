package com.metalplan.feature.plan.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalplan.domain.common.DomainResult
import com.metalplan.domain.common.TimeProvider
import com.metalplan.feature.plan.domain.model.Exercise
import com.metalplan.feature.plan.domain.model.TrainingPlan
import com.metalplan.feature.plan.domain.model.Workout
import java.util.UUID
import com.metalplan.feature.plan.domain.usecase.CreateTrainingPlanUseCase
import com.metalplan.feature.plan.domain.usecase.GetTrainingPlanByIdUseCase
import com.metalplan.feature.plan.domain.usecase.UpdateTrainingPlanUseCase
import com.metalplan.feature.plan.presentation.model.TrainingPlanFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TrainingPlanDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createTrainingPlanUseCase: CreateTrainingPlanUseCase,
    private val updateTrainingPlanUseCase: UpdateTrainingPlanUseCase,
    private val getTrainingPlanByIdUseCase: GetTrainingPlanByIdUseCase,
    private val timeProvider: TimeProvider
) : ViewModel() {

    private val athleteId: String = checkNotNull(savedStateHandle["athleteId"])
    private val _formState = MutableStateFlow(TrainingPlanFormState(athleteId = athleteId))
    val formState: StateFlow<TrainingPlanFormState> = _formState.asStateFlow()
    private var currentWorkouts: List<Workout> = emptyList()

    fun loadPlan(planId: String) {
        viewModelScope.launch {
            val plan = getTrainingPlanByIdUseCase(planId) ?: return@launch
            currentWorkouts = plan.workouts
            _formState.value = _formState.value.copy(
                planId = plan.id,
                title = plan.title,
                goal = plan.goal,
                startDateEpochDay = plan.startDateEpochDay,
                endDateEpochDay = plan.endDateEpochDay,
                isActive = plan.isActive,
                createdAt = plan.createdAt
            )
        }
    }

    fun onTitleChanged(value: String) { _formState.value = _formState.value.copy(title = value) }
    fun onGoalChanged(value: String) { _formState.value = _formState.value.copy(goal = value) }
    fun onStartDateChanged(value: Long) { _formState.value = _formState.value.copy(startDateEpochDay = value) }
    fun onEndDateChanged(value: Long) { _formState.value = _formState.value.copy(endDateEpochDay = value) }
    fun onIsActiveChanged(value: Boolean) { _formState.value = _formState.value.copy(isActive = value) }

    fun save(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _formState.value = _formState.value.copy(isSaving = true, errorMessage = null)
            val now = timeProvider.nowMillis()
            val plan = TrainingPlan(
                id = _formState.value.planId ?: UUID.randomUUID().toString(),
                athleteId = _formState.value.athleteId,
                title = _formState.value.title,
                goal = _formState.value.goal,
                startDateEpochDay = _formState.value.startDateEpochDay,
                endDateEpochDay = _formState.value.endDateEpochDay,
                isActive = _formState.value.isActive,
                workouts = currentWorkouts.ifEmpty { defaultWorkouts(_formState.value.planId) },
                createdAt = _formState.value.createdAt ?: now,
                updatedAt = now
            )
            val result = if (_formState.value.planId == null) {
                createTrainingPlanUseCase(plan)
            } else {
                updateTrainingPlanUseCase(plan)
            }
            _formState.value = when (result) {
                is DomainResult.Error -> _formState.value.copy(isSaving = false, errorMessage = result.message)
                is DomainResult.Success -> {
                    onSuccess()
                    _formState.value.copy(isSaving = false)
                }
            }
        }
    }

    private fun defaultWorkouts(planId: String?): List<Workout> {
        val normalizedPlanId = planId ?: UUID.randomUUID().toString()
        val workoutId = UUID.randomUUID().toString()
        return listOf(
            Workout(
                id = workoutId,
                trainingPlanId = normalizedPlanId,
                name = "Strength Session",
                dayOfWeek = 1,
                position = 0,
                exercises = listOf(
                    Exercise(
                        workoutId = workoutId,
                        name = "Back Squat",
                        sets = 5,
                        reps = 5,
                        restSeconds = 120,
                        notes = "Build foundational strength",
                        position = 0
                    )
                )
            )
        )
    }
}
