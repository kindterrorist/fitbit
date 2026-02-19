package com.metalplan.feature.athlete.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.metalplan.feature.athlete.presentation.screen.AthleteDetailRoute
import com.metalplan.feature.athlete.presentation.screen.AthleteListRoute

const val athleteListRoute = "athlete/list"
private const val athleteDetailRoute = "athlete/detail"

fun NavGraphBuilder.athleteGraph(
    navController: NavController,
    onViewPlans: (String) -> Unit
) {
fun NavGraphBuilder.athleteGraph(navController: NavController) {
    composable(route = athleteListRoute) {
        AthleteListRoute(
            onAddAthlete = { navController.navigate(athleteDetailRoute) },
            onAthleteClicked = { id -> navController.navigate("$athleteDetailRoute/$id") }
        )
    }

    composable(
        route = "$athleteDetailRoute/{athleteId}",
        arguments = listOf(navArgument("athleteId") { type = NavType.StringType })
    ) { backStackEntry ->
        AthleteDetailRoute(
            athleteId = backStackEntry.arguments?.getString("athleteId"),
            onSaved = { navController.popBackStack() },
            onViewPlans = onViewPlans
            onSaved = { navController.popBackStack() }
        )
    }

    composable(route = athleteDetailRoute) {
        AthleteDetailRoute(
            athleteId = null,
            onSaved = { navController.popBackStack() },
            onViewPlans = onViewPlans
            onSaved = { navController.popBackStack() }
        )
    }
}
