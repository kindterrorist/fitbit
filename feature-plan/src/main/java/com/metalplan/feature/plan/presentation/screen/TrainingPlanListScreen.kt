package com.metalplan.feature.plan.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.metalplan.feature.plan.R
import com.metalplan.feature.plan.presentation.model.TrainingPlanListUiState
import com.metalplan.feature.plan.presentation.viewmodel.TrainingPlanListViewModel

@Composable
fun TrainingPlanListRoute(
    onCreatePlan: () -> Unit,
    onEditPlan: (String) -> Unit,
    viewModel: TrainingPlanListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TrainingPlanListScreen(
        state = state,
        onCreatePlan = onCreatePlan,
        onEditPlan = onEditPlan,
        onDeletePlan = viewModel::deletePlan,
        onActiveOnlyChanged = viewModel::onActiveFilterChanged
    )
}

@Composable
fun TrainingPlanListScreen(
    state: TrainingPlanListUiState,
    onCreatePlan: () -> Unit,
    onEditPlan: (String) -> Unit,
    onDeletePlan: (String) -> Unit,
    onActiveOnlyChanged: (Boolean) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePlan) {
                Text(text = stringResource(id = R.string.create_plan_short))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Checkbox(checked = state.activeOnly, onCheckedChange = onActiveOnlyChanged)
                Text(text = stringResource(id = R.string.filter_active_plans))
            }

            when {
                state.isLoading -> Text(stringResource(id = R.string.loading_plans))
                state.errorMessage != null -> Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                state.isEmpty -> Text(stringResource(id = R.string.empty_plans))
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.plans, key = { it.id }) { plan ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = plan.title, style = MaterialTheme.typography.titleMedium)
                                Text(text = plan.goal)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(onClick = { onEditPlan(plan.id) }) {
                                        Text(stringResource(id = R.string.edit))
                                    }
                                    TextButton(onClick = { onDeletePlan(plan.id) }) {
                                        Text(stringResource(id = R.string.delete))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
