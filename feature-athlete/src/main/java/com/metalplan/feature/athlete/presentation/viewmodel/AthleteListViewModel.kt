package com.metalplan.feature.athlete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalplan.feature.athlete.domain.usecase.DeleteAthleteUseCase
import com.metalplan.feature.athlete.domain.usecase.ObserveAthletesUseCase
import com.metalplan.feature.athlete.presentation.model.AthleteListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AthleteListViewModel @Inject constructor(
    private val observeAthletesUseCase: ObserveAthletesUseCase,
    private val deleteAthleteUseCase: DeleteAthleteUseCase
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val activeOnly = MutableStateFlow(false)

    val uiState: StateFlow<AthleteListUiState> = combine(query, activeOnly) { q, active ->
        q to active
    }.flatMapLatest { (q, active) ->
        observeAthletesUseCase(q.ifBlank { null }, active)
            .map { athletes ->
                AthleteListUiState(
                    query = q,
                    activeOnly = active,
                    isLoading = false,
                    athletes = athletes,
                    errorMessage = null
                )
            }
    }.catch { throwable ->
        emit(
            AthleteListUiState(
                query = query.value,
                activeOnly = activeOnly.value,
                isLoading = false,
                athletes = emptyList(),
                errorMessage = throwable.message ?: "Unable to load athletes"
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AthleteListUiState()
    )

    fun onSearchChanged(value: String) {
        query.value = value
    }

    fun onActiveFilterChanged(value: Boolean) {
        activeOnly.value = value
    }

    fun deleteAthlete(id: String) {
        viewModelScope.launch { deleteAthleteUseCase(id) }
    }
}
