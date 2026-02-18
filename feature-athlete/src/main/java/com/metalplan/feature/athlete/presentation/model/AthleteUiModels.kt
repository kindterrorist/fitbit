package com.metalplan.feature.athlete.presentation.model

import com.metalplan.feature.athlete.domain.model.Athlete

sealed interface AthleteListUiState {
    data object Loading : AthleteListUiState
    data object Empty : AthleteListUiState
    data class Content(val athletes: List<Athlete>) : AthleteListUiState
    data class Error(val message: String) : AthleteListUiState
}

data class AthleteFormState(
    val id: String? = null,
    val fullName: String = "",
    val email: String = "",
    val sport: String = "",
    val active: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)
