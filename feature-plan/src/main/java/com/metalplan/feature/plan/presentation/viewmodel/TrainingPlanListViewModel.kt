package com.metalplan.feature.plan.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalplan.feature.plan.domain.usecase.DeleteTrainingPlanUseCase
import com.metalplan.feature.plan.domain.usecase.ObserveTrainingPlansByAthleteUseCase
import com.metalplan.feature.plan.presentation.model.TrainingPlanListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TrainingPlanListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeByAthleteUseCase: ObserveTrainingPlansByAthleteUseCase,
    private val deleteTrainingPlanUseCase: DeleteTrainingPlanUseCase
) : ViewModel() {

    private val athleteId: String = checkNotNull(savedStateHandle["athleteId"])
    private val activeOnly = MutableStateFlow(true)

    val uiState: StateFlow<TrainingPlanListUiState> = activeOnly.flatMapLatest { onlyActive ->
        observeByAthleteUseCase(athleteId, onlyActive).map { plans ->
            TrainingPlanListUiState(
                activeOnly = onlyActive,
                isLoading = false,
                plans = plans,
                errorMessage = null
            )
        }
    }.catch { throwable ->
        emit(
            TrainingPlanListUiState(
                activeOnly = activeOnly.value,
                isLoading = false,
                plans = emptyList(),
                errorMessage = throwable.message ?: "Unable to load plans"
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TrainingPlanListUiState()
    )

    fun onActiveFilterChanged(value: Boolean) {
        activeOnly.value = value
    }

    fun deletePlan(planId: String) {
        viewModelScope.launch { deleteTrainingPlanUseCase(planId) }
    }
}
