package com.example.proyectofinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ui.home.HomeScreen
import com.example.proyectofinal.ui.login.LoginScreen
import com.example.proyectofinal.ui.matches.MyPredictionsScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.proyectofinal.ui.groups.GroupsScreen
import com.example.proyectofinal.ui.groups.GroupDetailScreen
import com.example.proyectofinal.ui.matches.MatchesScreen
import com.example.proyectofinal.ui.profile.ProfileScreen
import com.example.proyectofinal.ui.stadiums.StadiumsScreen
import com.example.proyectofinal.ui.matches.MatchDetailScreen
import com.example.proyectofinal.ui.login.RegisterScreen
import com.example.proyectofinal.ui.stadiums.StadiumDetailScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.HOME) {
            MainScreen()
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.HOME) },
                    label = { Text("Inicio") },
                    icon = { Text("🏠") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.GROUPS) },
                    label = { Text("Grupos") },
                    icon = { Text("👥") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.MATCHES) },
                    label = { Text("Partidos") },
                    icon = { Text("⚽") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.STADIUMS) },
                    label = { Text("Mapa") },
                    icon = { Text("🗺️") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.PROFILE) },
                    label = { Text("Perfil") },
                    icon = { Text("👤") }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME) {
                HomeScreen()
            }

            composable(Routes.GROUPS) {
                GroupsScreen(
                    onGroupClick = { groupId ->
                        navController.navigate(Routes.groupDetail(groupId))
                    }
                )
            }

            composable(Routes.MATCHES) {
                MatchesScreen(
                    onMatchClick = { id ->
                        navController.navigate(
                            Routes.matchDetail(id)
                        )
                    }
                )
            }
            composable(Routes.MATCH_DETAIL) { backStackEntry ->

                val matchId =
                    backStackEntry.arguments
                        ?.getString("matchId")
                        ?.toIntOrNull() ?: 0

                MatchDetailScreen(matchId)
            }

            composable(Routes.STADIUMS) {
                StadiumsScreen(
                    onStadiumClick = { stadiumId ->
                        navController.navigate(Routes.stadiumDetail(stadiumId))
                    }
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0)
                        }
                    },
                    onMyPredictions = {
                        navController.navigate(Routes.MY_PREDICTIONS)
                    }
                )
            }
            composable(Routes.GROUP_DETAIL) { backStackEntry ->

                val groupId = backStackEntry.arguments
                    ?.getString("groupId")
                    ?.toIntOrNull() ?: 0

                GroupDetailScreen(groupId = groupId)
            }
            composable(Routes.MY_PREDICTIONS) {
                MyPredictionsScreen()
            }
            composable(Routes.STADIUM_DETAIL) { backStackEntry ->

                val stadiumId = backStackEntry.arguments
                    ?.getString("stadiumId")
                    ?.toIntOrNull() ?: 0

                StadiumDetailScreen(stadiumId = stadiumId)
            }
        }
    }
}