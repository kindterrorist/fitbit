package com.metalplan.feature.plan.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.metalplan.feature.plan.R
import com.metalplan.feature.plan.presentation.viewmodel.TrainingPlanDetailViewModel

@Composable
fun TrainingPlanDetailRoute(
    planId: String?,
    onSaved: () -> Unit,
    viewModel: TrainingPlanDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(planId) {
        planId?.let(viewModel::loadPlan)
    }
    val state by viewModel.formState.collectAsStateWithLifecycle()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = viewModel::onTitleChanged,
                label = { Text(stringResource(id = R.string.plan_title)) }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.goal,
                onValueChange = viewModel::onGoalChanged,
                label = { Text(stringResource(id = R.string.plan_goal)) }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.startDateEpochDay.toString(),
                onValueChange = { value -> viewModel.onStartDateChanged(value.toLongOrNull() ?: 0L) },
                label = { Text(stringResource(id = R.string.plan_start_epoch_day)) }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.endDateEpochDay.toString(),
                onValueChange = { value -> viewModel.onEndDateChanged(value.toLongOrNull() ?: 0L) },
                label = { Text(stringResource(id = R.string.plan_end_epoch_day)) }
            )
            Checkbox(checked = state.isActive, onCheckedChange = viewModel::onIsActiveChanged)
            state.errorMessage?.let { message ->
                Text(text = message, color = MaterialTheme.colorScheme.error)
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving,
                onClick = { viewModel.save(onSaved) }
            ) {
                Text(stringResource(id = R.string.save_plan))
            }
        }
    }
}
