package com.example.proyectofinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ui.home.HomeScreen
import com.example.proyectofinal.ui.login.LoginScreen
import com.example.proyectofinal.ui.matches.MyPredictionsScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.proyectofinal.data.datastore.UserPreferences
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

    val context = LocalContext.current
    val navController = rememberNavController()
    val preferences = remember { UserPreferences(context) }
    val token by preferences.token.collectAsState(initial = "loading")

    if (token == "loading") {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = if (!token.isNullOrEmpty()) Routes.HOME else Routes.LOGIN
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Routes.HOME,
                    onClick = {
                        if (currentRoute != Routes.HOME) {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) { inclusive = true }
                            }
                        }
                    },
                    label = { Text("Inicio") },
                    icon = { Icon(androidx.compose.material.icons.Icons.Default.Home, null) }
                )

                NavigationBarItem(
                    selected = currentRoute == Routes.GROUPS || currentRoute?.startsWith("group_detail") == true,
                    onClick = {
                        if (currentRoute != Routes.GROUPS) {
                            navController.navigate(Routes.GROUPS)
                        }
                    },
                    label = { Text("Grupos") },
                    icon = { Icon(androidx.compose.material.icons.Icons.Default.Groups, null) }
                )

                NavigationBarItem(
                    selected = currentRoute == Routes.MATCHES || currentRoute?.startsWith("match_detail") == true,
                    onClick = {
                        if (currentRoute != Routes.MATCHES) {
                            navController.navigate(Routes.MATCHES)
                        }
                    },
                    label = { Text("Partidos") },
                    icon = { Icon(androidx.compose.material.icons.Icons.Default.SportsSoccer, null) }
                )

                NavigationBarItem(
                    selected = currentRoute == Routes.STADIUMS || currentRoute?.startsWith("stadium_detail") == true,
                    onClick = {
                        if (currentRoute != Routes.STADIUMS) {
                            navController.navigate(Routes.STADIUMS)
                        }
                    },
                    label = { Text("Mapa") },
                    icon = { Icon(androidx.compose.material.icons.Icons.Default.Stadium, null) }
                )

                NavigationBarItem(
                    selected = currentRoute == Routes.PROFILE,
                    onClick = {
                        if (currentRoute != Routes.PROFILE) {
                            navController.navigate(Routes.PROFILE)
                        }
                    },
                    label = { Text("Perfil") },
                    icon = { Icon(androidx.compose.material.icons.Icons.Default.Person, null) }
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
                HomeScreen(
                    onNavigateToGroups = { navController.navigate(Routes.GROUPS) },
                    onNavigateToMatches = { navController.navigate(Routes.MATCHES) },
                    onNavigateToStadiums = { navController.navigate(Routes.STADIUMS) },
                    onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
                )
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