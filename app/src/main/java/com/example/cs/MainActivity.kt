package com.example.cs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.cs.data.db.DatabaseProvider
import com.example.cs.data.db.GuideInitializer
import com.example.cs.data.db.NadeEntity
import com.example.cs.data.db.NadeInitializer
import com.example.cs.data.db.PositionInitializer
import com.example.cs.ui.screens.GuidesScreen
import com.example.cs.ui.screens.HomeScreen
import com.example.cs.ui.screens.MapDetailScreen
import com.example.cs.ui.screens.MapsListScreen
import com.example.cs.ui.screens.NadeDetailScreen
import com.example.cs.ui.screens.PositionsScreen
import com.example.cs.ui.screens.SplashScreen
import com.example.cs.ui.theme.CsTheme
import com.example.cs.ui.viewmodels.MapsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            PositionInitializer.initPositionsIfEmpty(db)
            NadeInitializer.initNadesIfEmpty(db)
            GuideInitializer.initGuidesIfEmpty(db)
        }

        setContent {
            CsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // routes (без новых файлов)
    val ROUTE_SPLASH = "splash"
    val ROUTE_HOME = "home"

    // граф раскидок
    val ROUTE_SMOKES_GRAPH = "smokes_graph"
    val ROUTE_MAPS = "maps"
    val ROUTE_MAP_DETAIL = "map_detail/{mapId}"
    val ROUTE_NADE_DETAIL = "nade_detail/{mapId}/{nadeId}"

    fun mapDetailRoute(mapId: String) = "map_detail/$mapId"
    fun nadeDetailRoute(mapId: String, nadeId: String) = "nade_detail/$mapId/$nadeId"

    NavHost(
        navController = navController,
        startDestination = ROUTE_SPLASH
    ) {
        composable(ROUTE_SPLASH) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(ROUTE_HOME) {
                        popUpTo(ROUTE_SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(ROUTE_HOME) {
            HomeScreen(
                onCategoryClick = { categoryId ->
                    when (categoryId) {
                        "smokes" -> {
                            // ВАЖНО: не плодим один и тот же раздел, и восстанавливаем его состояние [web:485]
                            navController.navigate(ROUTE_SMOKES_GRAPH) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(ROUTE_HOME) { saveState = true }
                            }
                        }
                        "positions" -> navController.navigate("positions")
                        "guides" -> navController.navigate("guides")
                        else -> println("Category not implemented: $categoryId")
                    }
                }
            )
        }

        // Вложенный граф раскидок
        navigation(
            route = ROUTE_SMOKES_GRAPH,
            startDestination = ROUTE_MAPS
        ) {
            composable(ROUTE_MAPS) { backStackEntry ->
                // VM живёт пока граф ROUTE_SMOKES_GRAPH есть в back stack [web:482]
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(ROUTE_SMOKES_GRAPH)
                }
                val mapsVm: MapsViewModel = viewModel(parentEntry)

                MapsListScreen(
                    viewModel = mapsVm,
                    onMapClick = { mapId ->
                        navController.navigate(mapDetailRoute(mapId)) {
                            launchSingleTop = true
                        }
                    },
                    onBackClick = {
                        // Нормальный “назад”: вернуться на HOME и сохранить состояние графа [web:498]
                        navController.popBackStack(ROUTE_HOME, inclusive = false, saveState = true)
                    }
                )
            }

            composable(ROUTE_MAP_DETAIL) { backStackEntry ->
                val mapId = backStackEntry.arguments?.getString("mapId") ?: ""

                MapDetailScreen(
                    mapId = mapId,
                    onBackClick = { navController.popBackStack() },
                    onNadeClick = { nadeId ->
                        navController.navigate(nadeDetailRoute(mapId, nadeId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(ROUTE_NADE_DETAIL) { backStackEntry ->
                val mapId = backStackEntry.arguments?.getString("mapId") ?: ""
                val nadeId = backStackEntry.arguments?.getString("nadeId") ?: ""

                val context = LocalContext.current
                val db = remember { DatabaseProvider.getDatabase(context) }
                val scope = rememberCoroutineScope()

                var nadeEntity by remember { mutableStateOf<NadeEntity?>(null) }

                LaunchedEffect(mapId, nadeId) {
                    scope.launch(Dispatchers.IO) {
                        val list = db.nadeDao().getNadesForMap(mapId)
                        nadeEntity = list.find { it.id == nadeId }
                    }
                }

                nadeEntity?.let { entity ->
                    val uiNade = com.example.cs.data.models.Nade(
                        id = entity.id,
                        mapId = entity.mapId,
                        title = entity.title,
                        side = entity.side,
                        type = com.example.cs.data.models.NadeType.valueOf(entity.type),
                        throwType = com.example.cs.data.models.ThrowType.valueOf(entity.throwType),
                        difficulty = entity.difficulty,
                        position = entity.position,
                        aim = entity.aim,
                        description = entity.description,
                        landingSpot = entity.landingSpot,
                        images = com.example.cs.data.models.NadeImages(
                            position = entity.imagePosition.orEmpty(),
                            aim = entity.imageAim.orEmpty(),
                            result = entity.imageResult.orEmpty()
                        )
                    )

                    NadeDetailScreen(
                        nade = uiNade,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        composable("positions") {
            PositionsScreen(onBackClick = { navController.popBackStack() })
        }

        composable("guides") {
            GuidesScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
