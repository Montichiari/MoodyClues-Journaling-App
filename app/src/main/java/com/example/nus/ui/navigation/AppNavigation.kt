package com.example.nus.ui.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.nus.ui.screens.ClientsScreen
import com.example.nus.ui.screens.CounsellorHomeScreen
import com.example.nus.ui.screens.FeelScreen
import com.example.nus.ui.screens.HomeScreen
import com.example.nus.ui.screens.LifestyleLoggedScreen
import com.example.nus.ui.screens.LifestyleScreen
import com.example.nus.ui.screens.LoginScreen
import com.example.nus.ui.screens.MoodScreen
import com.example.nus.ui.screens.RegisterScreen
import com.example.nus.ui.screens.JournalScreen
import com.example.nus.ui.screens.JournalDetailScreen
import com.example.nus.viewmodel.FeelViewModel
import com.example.nus.viewmodel.JournalViewModel
import com.example.nus.viewmodel.LifestyleViewModel
import com.example.nus.viewmodel.MoodViewModel
import com.example.nus.viewmodel.UserSessionViewModel

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
    object Journal:Screen("journal", "Journal")
    object JournalForClient : Screen("journal/{clientId}", "Journal") {
        fun createRoute(clientId: String) = "journal/${android.net.Uri.encode(clientId)}"
    }

    // Screen.kt
    object JournalDetail {
        private const val base = "journalDetail"
        fun createRoute(entryId: String) = "$base/$entryId"
        const val route = "$base/{entryId}"
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Mood,
        Screen.Lifestyle
    )

    // ViewModels
    val userSessionViewModel: UserSessionViewModel = viewModel()
    val moodViewModel: MoodViewModel = viewModel()
    val lifestyleViewModel: LifestyleViewModel = viewModel()
    val feelViewModel: FeelViewModel = viewModel()
    val journalViewModel: JournalViewModel = viewModel()


    // 获取当前导航状态
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 定义需要显示底部导航栏的页面
    val screensWithBottomBar = listOf(
        Screen.Home.route,
        Screen.Mood.route,
        Screen.Lifestyle.route,
        Screen.Feel.route,
        Screen.LifestyleLogged.route
    )

    // 检查当前页面是否需要显示底部导航栏
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
                                    Screen.Home -> {
                                        if (selected) {
                                            Icon(Icons.Filled.Home, contentDescription = null)
                                        } else {
                                            Icon(Icons.Outlined.Home, contentDescription = null)
                                        }
                                    }
                                    Screen.Mood -> {
                                        if (selected) {
                                            Icon(Icons.Filled.Face, contentDescription = null)
                                        } else {
                                            Icon(Icons.Outlined.Face, contentDescription = null)
                                        }
                                    }
                                    Screen.Lifestyle -> {
                                        if (selected) {
                                            Icon(Icons.Filled.DateRange, contentDescription = null)
                                        } else {
                                            Icon(Icons.Outlined.DateRange, contentDescription = null)
                                        }
                                    }
                                    else -> {
                                        Icon(Icons.Outlined.Home, contentDescription = null)
                                    }
                                }
                            },
                            label = { Text(screen.title) },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
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
                        val destination = if (userType == com.example.nus.viewmodel.UserType.COUNSELLOR) {
                            Screen.CounsellorHome.route
                        } else {
                            Screen.Home.route
                        }
                        navController.navigate(destination) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onSignUpClick = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToMood = {
                        navController.navigate(Screen.Mood.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToLifestyle = {
                        navController.navigate(Screen.Lifestyle.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.CounsellorHome.route) {
                CounsellorHomeScreen(
                    onClientsClick = {
                        navController.navigate(Screen.Clients.route)
                    },
                    onInviteClick = {
                        // TODO: Navigate to invite screen
                    },
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
                    onInviteClick = { /* TODO */ },
                    onJournalClick = { client ->
                        // Navigate with this navController (do NOT create a new one inside ClientsScreen)
                        navController.navigate(Screen.JournalForClient.createRoute(client.clientId))
                    }
                )
            }

            composable(Screen.Mood.route) {
                MoodScreen(
                    viewModel = moodViewModel,
                    userId = userSessionViewModel.userId.value,
                    onNavigateToFeel = {
                        navController.navigate(Screen.Feel.route)
                    }
                )
            }
            composable(Screen.Lifestyle.route) {
                val currentUserId = userSessionViewModel.userId.value
                println("AppNavigation: Navigating to LifestyleScreen with userId = '$currentUserId'")
                LifestyleScreen(
                    viewModel = lifestyleViewModel,
                    userId = currentUserId,
                    onNavigateToLifestyleLogged = {
                        navController.navigate(Screen.LifestyleLogged.route)
                    }
                )
            }
            composable(Screen.Feel.route) { // 这里是导航回主页的方式
                FeelScreen(
                    viewModel = feelViewModel,
                    moodViewModel = moodViewModel, // 传递MoodViewModel
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.LifestyleLogged.route) {
                LifestyleLoggedScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )


            }
            // List of journals for one client
            composable(
                route = Screen.Journal.route
            ) { backStackEntry ->
                val clientId = backStackEntry.arguments?.getString("clientId") ?: ""
                val journalVm: JournalViewModel = viewModel() // or hiltViewModel()

                JournalScreen(
                    viewModel = journalVm,
                    clientUserId = clientId,
                    navController = navController
                )
            }

// From Clients list to a client's journal
            composable(
                route = Screen.JournalForClient.route,
                arguments = listOf(navArgument("clientId") { type = NavType.StringType })
            ) { backStackEntry ->
                val clientId = backStackEntry.arguments?.getString("clientId") ?: return@composable
                val journalVm: JournalViewModel = viewModel()

                com.example.nus.ui.screens.JournalScreen(
                    viewModel = journalVm,
                    clientUserId = clientId,
                    navController = navController
                )
            }









        }
    }
}