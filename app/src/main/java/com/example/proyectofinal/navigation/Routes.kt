package com.example.proyectofinal.navigation

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val GROUPS = "groups"
    const val MATCHES = "matches"
    const val STADIUMS = "stadiums"
    const val PROFILE = "profile"
    const val GROUP_DETAIL = "group_detail/{groupId}"

    fun groupDetail(groupId: Int) = "group_detail/$groupId"
    const val MATCH_DETAIL = "match_detail/{matchId}"

    fun matchDetail(matchId: Int) =
        "match_detail/$matchId"

    const val STADIUM_DETAIL = "stadium_detail/{stadiumId}"

    fun stadiumDetail(stadiumId: Int) =
        "stadium_detail/$stadiumId"
    const val REGISTER = "register"

    const val MY_PREDICTIONS = "my_predictions"
}