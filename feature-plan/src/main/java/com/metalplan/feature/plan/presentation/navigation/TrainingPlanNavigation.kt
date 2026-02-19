package com.metalplan.feature.plan.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.metalplan.feature.plan.presentation.screen.TrainingPlanDetailRoute
import com.metalplan.feature.plan.presentation.screen.TrainingPlanListRoute

const val trainingPlanListRoute = "plan/list/{athleteId}"
private const val trainingPlanListBase = "plan/list"
private const val trainingPlanDetailBase = "plan/detail"

fun NavGraphBuilder.trainingPlanGraph(navController: NavController) {
    composable(
        route = trainingPlanListRoute,
        arguments = listOf(navArgument("athleteId") { type = NavType.StringType })
    ) { backStackEntry ->
        val athleteId = checkNotNull(backStackEntry.arguments?.getString("athleteId"))
        TrainingPlanListRoute(
            onCreatePlan = { navController.navigate(buildCreateTrainingPlanRoute(athleteId)) },
            onEditPlan = { planId -> navController.navigate(buildEditTrainingPlanRoute(athleteId, planId)) }
        )
    }

    composable(
        route = "$trainingPlanDetailBase/{athleteId}",
        arguments = listOf(navArgument("athleteId") { type = NavType.StringType })
    ) {
        TrainingPlanDetailRoute(planId = null, onSaved = { navController.popBackStack() })
    }

    composable(
        route = "$trainingPlanDetailBase/{athleteId}/{planId}",
        arguments = listOf(
            navArgument("athleteId") { type = NavType.StringType },
            navArgument("planId") { type = NavType.StringType }
        )
    ) { entry ->
        TrainingPlanDetailRoute(
            planId = entry.arguments?.getString("planId"),
            onSaved = { navController.popBackStack() }
        )
    }
}

fun buildTrainingPlanListRoute(athleteId: String): String = "$trainingPlanListBase/$athleteId"
fun buildCreateTrainingPlanRoute(athleteId: String): String = "$trainingPlanDetailBase/$athleteId"
fun buildEditTrainingPlanRoute(athleteId: String, planId: String): String = "$trainingPlanDetailBase/$athleteId/$planId"
