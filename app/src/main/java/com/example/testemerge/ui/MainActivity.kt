package com.example.testemerge.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testemerge.ui.home.HomeScreen
import com.example.testemerge.ui.login.LoginScreen
import com.example.testemerge.ui.theme.TestEmergeTheme

private const val LOGIN_PATH = "login"
private const val HOME_PATH = "home"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestEmergeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = LOGIN_PATH) {
                        composable(LOGIN_PATH) {
                            LoginScreen {
                                navController.navigate(HOME_PATH)
                            }
                        }
                        composable(HOME_PATH) {
                            HomeScreen {
                                navController.navigate(LOGIN_PATH)
                            }
                        }
                    }
                }
            }
        }
    }
}