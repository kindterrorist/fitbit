package com.metalplan.feature.athlete.presentation.screen

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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.metalplan.feature.athlete.R
import com.metalplan.feature.athlete.presentation.model.AthleteListUiState
import com.metalplan.feature.athlete.presentation.viewmodel.AthleteListViewModel

@Composable
fun AthleteListRoute(
    onAddAthlete: () -> Unit,
    onAthleteClicked: (String) -> Unit,
    viewModel: AthleteListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AthleteListScreen(
        state = state,
        onAddAthlete = onAddAthlete,
        onAthleteClicked = onAthleteClicked,
        onDelete = viewModel::deleteAthlete,
        onQueryChanged = viewModel::onSearchChanged,
        onActiveFilterChanged = viewModel::onActiveFilterChanged
    )
}

@Composable
fun AthleteListScreen(
    state: AthleteListUiState,
    onAddAthlete: () -> Unit,
    onAthleteClicked: (String) -> Unit,
    onDelete: (String) -> Unit,
    onQueryChanged: (String) -> Unit,
    onActiveFilterChanged: (Boolean) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAthlete) {
                Text(text = stringResource(id = R.string.add_athlete))
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
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.query,
                value = "",
                onValueChange = onQueryChanged,
                label = { Text(text = stringResource(id = R.string.search_athletes)) }
            )
            Row {
                Checkbox(checked = state.activeOnly, onCheckedChange = onActiveFilterChanged)
                Text(text = stringResource(id = R.string.filter_active_only))
            }

            when {
                state.isLoading -> Text(text = stringResource(id = R.string.loading))
                state.errorMessage != null -> Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                state.isEmpty -> Text(text = stringResource(id = R.string.empty_athletes))
                else -> {
                Checkbox(checked = false, onCheckedChange = onActiveFilterChanged)
                Text(text = stringResource(id = R.string.filter_active_only))
            }

            when (state) {
                AthleteListUiState.Empty -> Text(text = stringResource(id = R.string.empty_athletes))
                is AthleteListUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
                AthleteListUiState.Loading -> Text(text = stringResource(id = R.string.loading))
                is AthleteListUiState.Content -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.athletes, key = { it.id }) { athlete ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onAthleteClicked(athlete.id) }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = athlete.fullName, style = MaterialTheme.typography.titleMedium)
                                    Text(text = athlete.email)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        TextButton(onClick = { onAthleteClicked(athlete.id) }) {
                                            Text(text = stringResource(id = R.string.edit))
                                        }
                                        TextButton(onClick = { onDelete(athlete.id) }) {
                                            Text(text = stringResource(id = R.string.delete))
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
}
