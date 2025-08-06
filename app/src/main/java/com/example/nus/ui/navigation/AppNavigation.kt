package com.example.nus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nus.ui.screens.JournalScreen
import com.example.nus.ui.screens.JournalDetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "journalList"
    ) {
        // List screen
        composable("journalList") {
            JournalScreen(navController)
        }

        // Detail screen with argument
        composable(
            route = "journalDetail/{entryContent}",
            arguments = listOf(navArgument("entryContent") { type = NavType.StringType })
        ) { backStackEntry ->
            val content = backStackEntry.arguments?.getString("entryContent") ?: ""
            JournalDetailScreen(content)
        }
    }
}
