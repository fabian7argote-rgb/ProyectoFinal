package com.example.proyectofinal.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.proyectofinal.ui.groups.GroupDetailScreen
import com.example.proyectofinal.ui.groups.GroupsScreen
import com.example.proyectofinal.ui.home.HomeScreen
import com.example.proyectofinal.ui.login.LoginScreen
import com.example.proyectofinal.ui.login.RegisterScreen
import com.example.proyectofinal.ui.matches.MatchDetailScreen
import com.example.proyectofinal.ui.matches.MatchesScreen
import com.example.proyectofinal.ui.matches.MyPredictionsScreen
import com.example.proyectofinal.ui.profile.ProfileScreen
import com.example.proyectofinal.ui.stadiums.StadiumDetailScreen
import com.example.proyectofinal.ui.stadiums.StadiumsScreen
import androidx.compose.material.icons.Icons
import androidx.compose.ui.unit.dp

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

        composable(Routes.HOME) {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp)
                    .background(Color(0xFF061A14))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomItem("Inicio", Icons.Default.Home, currentRoute == Routes.HOME) {
                        navController.navigate(Routes.HOME)
                    }

                    BottomItem("Grupos", Icons.Default.Groups, currentRoute == Routes.GROUPS) {
                        navController.navigate(Routes.GROUPS)
                    }

                    BottomItem("Partidos", Icons.Default.SportsSoccer, currentRoute == Routes.MATCHES) {
                        navController.navigate(Routes.MATCHES)
                    }

                    BottomItem("Sedes", Icons.Default.LocationOn, currentRoute == Routes.STADIUMS) {
                        navController.navigate(Routes.STADIUMS)
                    }

                    BottomItem("Perfil", Icons.Default.Person, currentRoute == Routes.PROFILE) {
                        navController.navigate(Routes.PROFILE)
                    }
                }
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
                    onNavigateToGroups = {
                        navController.navigate(Routes.GROUPS)
                    },
                    onNavigateToMatches = {
                        navController.navigate(Routes.MATCHES)
                    },
                    onNavigateToStadiums = {
                        navController.navigate(Routes.STADIUMS)
                    },
                    onNavigateToProfile = {
                        navController.navigate(Routes.PROFILE)
                    },
                    onNextMatchClick = { matchId ->
                        navController.navigate(
                            Routes.matchDetail(matchId)
                        )
                    }
                )
            }

            composable(Routes.GROUPS) {
                GroupsScreen(
                    onGroupClick = { groupId ->
                        navController.navigate(Routes.groupDetail(groupId))
                    }
                )
            }

            composable(Routes.GROUP_DETAIL) { backStackEntry ->

                val groupId = backStackEntry.arguments
                    ?.getString("groupId")
                    ?.toIntOrNull() ?: 0

                GroupDetailScreen(
                    groupId = groupId,
                    onMatchClick = { matchId ->
                        navController.navigate(
                            Routes.matchDetail(matchId)
                        )
                    }
                )
            }

            composable(Routes.MATCHES) {
                MatchesScreen(
                    onMatchClick = { matchId ->
                        navController.navigate(Routes.matchDetail(matchId))
                    }
                )
            }

            composable(Routes.MATCH_DETAIL) { backStackEntry ->

                val matchId = backStackEntry.arguments
                    ?.getString("matchId")
                    ?.toIntOrNull() ?: 0

                MatchDetailScreen(
                    matchId = matchId,
                    onPredictionSaved = {

                         //Si Grupos ya existe en el historial,
                         // regresamos a esa misma pantalla.

                        val returnedToGroups = navController.popBackStack(
                            route = Routes.GROUPS,
                            inclusive = false
                        )

                        //Si el usuario abrió el partido desde otra
                         //pantalla y Grupos no está en el historial,
                         //navegamos a Grupos.
                        if (!returnedToGroups) {
                            navController.navigate(Routes.GROUPS) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable(Routes.STADIUMS) {
                StadiumsScreen(
                    onStadiumClick = { stadiumId ->
                        navController.navigate(Routes.stadiumDetail(stadiumId))
                    }
                )
            }

            composable(Routes.STADIUM_DETAIL) { backStackEntry ->
                val stadiumId = backStackEntry.arguments
                    ?.getString("stadiumId")
                    ?.toIntOrNull() ?: 0

                StadiumDetailScreen(stadiumId = stadiumId)
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

            composable(Routes.MY_PREDICTIONS) {
                MyPredictionsScreen()
            }
        }
    }
}

@Composable
fun BottomItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) Color(0xFFFFD166) else Color(0xFFCFEFE2)

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelSmall
        )
    }
}


