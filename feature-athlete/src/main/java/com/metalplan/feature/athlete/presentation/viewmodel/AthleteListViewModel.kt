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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AthleteListViewModel @Inject constructor(
    observeAthletesUseCase: ObserveAthletesUseCase,
    private val deleteAthleteUseCase: DeleteAthleteUseCase
) : ViewModel() {

    private val query = MutableStateFlow<String?>(null)
    private val activeOnly = MutableStateFlow(false)

    val uiState: StateFlow<AthleteListUiState> = combine(query, activeOnly) { search, onlyActive ->
        search to onlyActive
    }.combine(observeAthletesUseCase(null, false)) { filters, athletes ->
        filters to athletes
    }.catch {
        emit((null to false) to emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), (null to false) to emptyList())
        .let { state ->
            stateInUiState(state)
        }

    private fun stateInUiState(
        source: StateFlow<Pair<Pair<String?, Boolean>, List<com.metalplan.feature.athlete.domain.model.Athlete>>>
    ): StateFlow<AthleteListUiState> {
        val uiState = MutableStateFlow<AthleteListUiState>(AthleteListUiState.Loading)
        viewModelScope.launch {
            source.collect { (filters, athletes) ->
                val filtered = athletes.filter { athlete ->
                    val queryValue = filters.first
                    val activeFilter = filters.second
                    val matchesQuery = queryValue.isNullOrBlank() ||
                        athlete.fullName.contains(queryValue, ignoreCase = true) ||
                        athlete.email.contains(queryValue, ignoreCase = true)
                    val matchesActive = !activeFilter || athlete.active
                    matchesQuery && matchesActive
                }
                uiState.value = when {
                    filtered.isEmpty() -> AthleteListUiState.Empty
                    else -> AthleteListUiState.Content(filtered)
                }
            }
        }
        return uiState
    }

    fun onSearchChanged(value: String) {
        query.value = value.ifBlank { null }
    }

    fun onActiveFilterChanged(value: Boolean) {
        activeOnly.value = value
    }

    fun deleteAthlete(id: String) {
        viewModelScope.launch { deleteAthleteUseCase(id) }
    }
}
