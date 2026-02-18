package com.metalplan.feature.athlete.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalplan.domain.common.DomainResult
import com.metalplan.feature.athlete.domain.model.Athlete
import com.metalplan.feature.athlete.domain.usecase.CreateAthleteUseCase
import com.metalplan.feature.athlete.domain.usecase.GetAthleteByIdUseCase
import com.metalplan.feature.athlete.domain.usecase.UpdateAthleteUseCase
import com.metalplan.feature.athlete.presentation.model.AthleteFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AthleteDetailViewModel @Inject constructor(
    private val createAthleteUseCase: CreateAthleteUseCase,
    private val updateAthleteUseCase: UpdateAthleteUseCase,
    private val getAthleteByIdUseCase: GetAthleteByIdUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(AthleteFormState())
    val formState: StateFlow<AthleteFormState> = _formState.asStateFlow()

    fun loadAthlete(athleteId: String) {
        viewModelScope.launch {
            val athlete = getAthleteByIdUseCase(athleteId) ?: return@launch
            _formState.value = _formState.value.copy(
                id = athlete.id,
                fullName = athlete.fullName,
                email = athlete.email,
                sport = athlete.sport,
                active = athlete.active
            )
        }
    }

    fun onNameChanged(value: String) { _formState.value = _formState.value.copy(fullName = value) }
    fun onEmailChanged(value: String) { _formState.value = _formState.value.copy(email = value) }
    fun onSportChanged(value: String) { _formState.value = _formState.value.copy(sport = value) }
    fun onActiveChanged(value: Boolean) { _formState.value = _formState.value.copy(active = value) }

    fun save(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _formState.value = _formState.value.copy(isSaving = true, errorMessage = null)
            val now = System.currentTimeMillis()
            val athlete = Athlete(
                id = _formState.value.id ?: java.util.UUID.randomUUID().toString(),
                fullName = _formState.value.fullName,
                email = _formState.value.email,
                sport = _formState.value.sport,
                active = _formState.value.active,
                createdAt = now,
                updatedAt = now
            )
            val result = if (_formState.value.id == null) createAthleteUseCase(athlete) else updateAthleteUseCase(athlete)
            _formState.value = when (result) {
                is DomainResult.Error -> _formState.value.copy(isSaving = false, errorMessage = result.message)
                is DomainResult.Success -> {
                    onSuccess()
                    _formState.value.copy(isSaving = false)
                }
            }
        }
    }
}
