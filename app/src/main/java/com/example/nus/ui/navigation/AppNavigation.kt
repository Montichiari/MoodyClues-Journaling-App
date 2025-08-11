package com.example.nus.ui.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nus.ui.screens.*
import com.example.nus.viewmodel.*

sealed class Screen(val route: String, val title: String) {
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Register")
    object Home : Screen("home", "Home")
    object CounsellorHome : Screen("counsellor_home", "Counsellor Home")
    object Clients : Screen("clients", "Clients")
    object Mood : Screen("mood", "Mood")
    object Lifestyle : Screen("lifestyle", "Lifestyle")
    object Feel : Screen("feel", "Feel")
    object LifestyleLogged : Screen("lifestyle_logged", "Lifestyle Logged")
    object Journal : Screen("journal", "Journal")

    object JournalForClient : Screen("journal/{clientId}", "Journal") {
        fun createRoute(clientId: String) = "journal/${Uri.encode(clientId)}"
    }

    object JournalDetail : Screen("journalDetail/{entryId}", "Journal Detail") {
        fun createRoute(entryId: String) = "journalDetail/${Uri.encode(entryId)}"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Mood, Screen.Lifestyle)

    // ViewModels (scoped to NavHost)
    val userSessionViewModel: UserSessionViewModel = viewModel()
    val moodViewModel: MoodViewModel = viewModel()
    val lifestyleViewModel: LifestyleViewModel = viewModel()
    val feelViewModel: FeelViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screensWithBottomBar = listOf(
        Screen.Home.route, Screen.Mood.route, Screen.Lifestyle.route,
        Screen.Feel.route, Screen.LifestyleLogged.route
    )
    val shouldShowBottomBar = currentDestination?.route in screensWithBottomBar

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    items.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = {
                                when (screen) {
                                    Screen.Home -> if (selected) Icon(Icons.Filled.Home, null) else Icon(Icons.Outlined.Home, null)
                                    Screen.Mood -> if (selected) Icon(Icons.Filled.Face, null) else Icon(Icons.Outlined.Face, null)
                                    Screen.Lifestyle -> if (selected) Icon(Icons.Filled.DateRange, null) else Icon(Icons.Outlined.DateRange, null)
                                    else -> Icon(Icons.Outlined.Home, null)
                                }
                            },
                            label = { Text(screen.title) },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = { userId, showEmotion, email, password, userType ->
                        userSessionViewModel.setUserSession(userId, showEmotion, email, password)
                        val destination = if (userType == UserType.COUNSELLOR)
                            Screen.CounsellorHome.route else Screen.Home.route
                        navController.navigate(destination) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onSignUpClick = { navController.navigate(Screen.Register.route) }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = { navController.popBackStack() }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToMood = {
                        navController.navigate(Screen.Mood.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    },
                    onNavigateToLifestyle = {
                        navController.navigate(Screen.Lifestyle.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    }
                )
            }

            composable(Screen.CounsellorHome.route) {
                CounsellorHomeScreen(
                    onClientsClick = { navController.navigate(Screen.Clients.route) },
                    onInviteClick = { /* TODO */ },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.CounsellorHome.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Clients.route) {
                ClientsScreen(
                    counsellorId = userSessionViewModel.userId.value,
                    onBackClick = { navController.navigate(Screen.CounsellorHome.route) },
                    onInviteClick = { /* open invite screen */ },
                    onJournalClick = { clientId ->
                        navController.navigate(Screen.JournalForClient.createRoute(clientId))
                    }
                )
            }

            composable(Screen.Mood.route) {
                MoodScreen(
                    viewModel = moodViewModel,
                    userId = userSessionViewModel.userId.value,
                    onNavigateToFeel = { navController.navigate(Screen.Feel.route) }
                )
            }

            composable(Screen.Lifestyle.route) {
                val currentUserId = userSessionViewModel.userId.value
                LifestyleScreen(
                    viewModel = lifestyleViewModel,
                    userId = currentUserId,
                    onNavigateToLifestyleLogged = { navController.navigate(Screen.LifestyleLogged.route) }
                )
            }

            composable(Screen.Feel.route) {
                FeelScreen(
                    viewModel = feelViewModel,
                    moodViewModel = moodViewModel,
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    }
                )
            }

            composable(Screen.LifestyleLogged.route) {
                LifestyleLoggedScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    }
                )
            }

            // -------- Journals flow --------

            // List for a specific client (pass clientId)
            composable(
                route = Screen.JournalForClient.route,
                arguments = listOf(navArgument("clientId") { type = NavType.StringType })
            ) { backStackEntry ->
                val clientId = backStackEntry.arguments?.getString("clientId") ?: return@composable
                val journalVm: JournalViewModel = viewModel()
                JournalScreen(
                    viewModel = journalVm,
                    clientUserId = clientId,
                    navController = navController
                )
            }

            // Detail by entryId
            composable(Screen.JournalDetail.route) { backStackEntry ->
                val entryId = backStackEntry.arguments?.getString("entryId") ?: return@composable
                val journalVm: JournalViewModel = viewModel()
                JournalDetailScreen(
                    viewModel = journalVm,
                    entryId = entryId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
