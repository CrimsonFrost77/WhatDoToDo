// navigation/AppNavigation.kt

package com.example.whatdotodo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.whatdotodo.ui.screen.LoginScreen
import com.example.whatdotodo.ui.screen.RegisterScreen
import com.example.whatdotodo.ui.screen.ToDoListScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("todo_list") { ToDoListScreen(navController) }
    }
}
