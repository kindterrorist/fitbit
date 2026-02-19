package com.metalplan.feature.athlete.presentation.screen

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
import com.metalplan.feature.athlete.R
import com.metalplan.feature.athlete.presentation.viewmodel.AthleteDetailViewModel

@Composable
fun AthleteDetailRoute(
    athleteId: String?,
    onSaved: () -> Unit,
    onViewPlans: (String) -> Unit,
    viewModel: AthleteDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(athleteId) {
        athleteId?.let(viewModel::loadAthlete)
    }
    val form by viewModel.formState.collectAsStateWithLifecycle()

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
                value = form.fullName,
                onValueChange = viewModel::onNameChanged,
                label = { Text(stringResource(id = R.string.athlete_name)) }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = form.email,
                onValueChange = viewModel::onEmailChanged,
                label = { Text(stringResource(id = R.string.athlete_email)) }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = form.sport,
                onValueChange = viewModel::onSportChanged,
                label = { Text(stringResource(id = R.string.athlete_sport)) }
            )
            Checkbox(checked = form.active, onCheckedChange = viewModel::onActiveChanged)
            form.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.save(onSaved) },
                enabled = !form.isSaving
            ) {
                Text(text = stringResource(id = R.string.save))
            }
            if (athleteId != null) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onViewPlans(athleteId) }
                ) {
                    Text(text = stringResource(id = R.string.view_plans))
                }
            }
        }
    }
}
