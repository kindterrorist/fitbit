package com.metalplan.fitbit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.metalplan.core.ui.theme.FitbitTheme
import com.metalplan.feature.athlete.presentation.navigation.athleteGraph
import com.metalplan.feature.athlete.presentation.navigation.athleteListRoute
import com.metalplan.feature.plan.presentation.navigation.buildTrainingPlanListRoute
import com.metalplan.feature.plan.presentation.navigation.trainingPlanGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitbitTheme {
                FitbitApp()
            }
        }
    }
}

@Composable
private fun FitbitApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = athleteListRoute
    ) {
        athleteGraph(
            navController = navController,
            onViewPlans = { athleteId -> navController.navigate(buildTrainingPlanListRoute(athleteId)) }
        )
        trainingPlanGraph(navController)
        athleteGraph(navController)
    }
}
