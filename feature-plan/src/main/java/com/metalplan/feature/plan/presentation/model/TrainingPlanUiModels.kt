package com.metalplan.feature.plan.presentation.model

import com.metalplan.feature.plan.domain.model.TrainingPlan

data class TrainingPlanListUiState(
    val activeOnly: Boolean = true,
    val isLoading: Boolean = true,
    val plans: List<TrainingPlan> = emptyList(),
    val errorMessage: String? = null
) {
    val isEmpty: Boolean = !isLoading && errorMessage == null && plans.isEmpty()
}

data class TrainingPlanFormState(
    val athleteId: String = "",
    val planId: String? = null,
    val title: String = "",
    val goal: String = "",
    val startDateEpochDay: Long = 0,
    val endDateEpochDay: Long = 0,
    val isActive: Boolean = true,
    val createdAt: Long? = null,
    val errorMessage: String? = null,
    val isSaving: Boolean = false
)
