package com.metalplan.feature.athlete.presentation.model

import com.metalplan.feature.athlete.domain.model.Athlete

data class AthleteListUiState(
    val query: String = "",
    val activeOnly: Boolean = false,
    val isLoading: Boolean = true,
    val athletes: List<Athlete> = emptyList(),
    val errorMessage: String? = null
) {
    val isEmpty: Boolean = !isLoading && errorMessage == null && athletes.isEmpty()
}

data class AthleteFormState(
    val id: String? = null,
    val fullName: String = "",
    val email: String = "",
    val sport: String = "",
    val active: Boolean = true,
    val createdAt: Long? = null,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)
