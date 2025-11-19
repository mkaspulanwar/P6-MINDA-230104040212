// Package sudah disesuaikan dengan ID Anda
package id.antasari.p6minda_230104040212

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.antasari.p6minda_230104040212.data.UserPrefsRepository
import id.antasari.p6minda_230104040212.ui.BottomNavBar
import id.antasari.p6minda_230104040212.ui.navigation.AppNavHost
import id.antasari.p6minda_230104040212.ui.navigation.Routes
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Gunakan tema langit (biru muda & putih)
            MindaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // --- Repo & State (DataStore) ---
                    val userPrefs = remember { UserPrefsRepository(this@MainActivity) }
                    val scope = rememberCoroutineScope()

                    // Ambil data dari DataStore
                    val storedName by userPrefs.userNameFlow.collectAsState(initial = null)
                    val onboardDone by userPrefs.onboardingCompletedFlow.collectAsState(initial = false)

                    // --- Navigasi ---
                    val navController = rememberNavController()
                    val currentRoute by navController.currentBackStackEntryAsState()
                    val route = currentRoute?.destination?.route

                    // Scaffold utama
                    Scaffold(
                        bottomBar = {
                            if (shouldShowBottomBar(route)) {
                                BottomNavBar(navController = navController)
                            }
                        },
                        floatingActionButton = {
                            if (shouldShowBottomBar(route)) {
                                FloatingActionButton(
                                    onClick = { navController.navigate(Routes.NEW) },
                                    modifier = Modifier.offset(y = 40.dp),
                                    containerColor = MaterialTheme.colorScheme.primary, // Warna biru muda dari tema
                                    contentColor = Color.White
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Tambah catatan baru"
                                    )
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.Center
                    ) { innerPadding ->
                        // Host Navigasi
                        AppNavHost(
                            navController = navController,
                            storedName = storedName,
                            hasCompletedOnboarding = onboardDone,
                            onSaveUserName = { name ->
                                scope.launch { userPrefs.saveUserName(name) }
                            },
                            onSetOnboardingCompleted = {
                                scope.launch { userPrefs.setOnboardingCompleted(true) }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    /** Bottom bar hanya tampil di 4 tab utama */
    private fun shouldShowBottomBar(route: String?): Boolean {
        return route in setOf(Routes.HOME, Routes.CALENDAR, Routes.INSIGHTS, Routes.SETTINGS)
    }
}