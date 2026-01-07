package com.example.cs.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cs.data.db.DatabaseProvider
import com.example.cs.data.db.PositionEntity
import com.example.cs.ui.theme.CardBackground
import com.example.cs.ui.theme.DarkBackground
import com.example.cs.ui.theme.TextPrimary
import com.example.cs.ui.theme.TextSecondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private data class MapUi(
    val id: String,
    val name: String,
    val mapImageName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PositionsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }

    val maps = remember {
        listOf(
            MapUi("mirage", "Mirage", "positions_mirage"),
            MapUi("dust2", "Dust 2", "positions_dust2"),
            MapUi("inferno", "Inferno", "positions_inferno"),
            MapUi("nuke", "Nuke", "positions_nuke"),
            MapUi("overpass", "Overpass", "positions_overpass"),
            MapUi("ancient", "Ancient", "positions_ancient"),
            MapUi("train", "Train", "positions_train"),
            MapUi("anubis", "Anubis", "positions_anubis"),
        )
    }

    var positionsByMap by remember { mutableStateOf<Map<String, List<PositionEntity>>>(emptyMap()) }

    LaunchedEffect(Unit) {
        positionsByMap = withContext(Dispatchers.IO) {
            val dao = db.positionDao()
            maps.associate { m ->
                m.id to dao.getPositionsForMap(m.id)
            }
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Позиции на картах",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←", fontSize = 24.sp, color = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(maps) { map ->
                val positions = positionsByMap[map.id].orEmpty()
                MapPositionsCard(
                    map = map,
                    positions = positions
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun MapPositionsCard(
    map: MapUi,
    positions: List<PositionEntity>
) {
    var expanded by remember { mutableStateOf(false) }
    var showFullMap by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val mapImageRes = remember(map.mapImageName) {
        context.resources.getIdentifier(map.mapImageName, "drawable", context.packageName)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = map.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (expanded) "Скрыть" else "Показать",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkBackground)
                            .clickable(enabled = mapImageRes != 0) { showFullMap = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (mapImageRes != 0) {
                            Image(
                                painter = painterResource(id = mapImageRes),
                                contentDescription = "${map.name} positions map",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = "Нет карты: ${map.mapImageName}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (positions.isEmpty()) {
                        Text(
                            text = "Нет позиций для этой карты",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    } else {
                        positions.forEach { pos ->
                            PositionRow(pos)
                        }
                    }
                }
            }
        }
    }

    if (showFullMap && mapImageRes != 0) {
        FullscreenImageDialog(
            resId = mapImageRes,
            title = map.name,
            onDismiss = { showFullMap = false }
        )
    }
}

@Composable
private fun FullscreenImageDialog(
    resId: Int,
    title: String,
    onDismiss: () -> Unit
) {
    // 1. Создаем InteractionSource для отключения визуального эффекта клика
    val interactionSource = remember { MutableInteractionSource() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                // Клик по фону закрывает диалог (без ripple-эффекта)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            // Переменные для зума и сдвига
            var scale by remember { mutableStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }

            Image(
                painter = painterResource(id = resId),
                contentDescription = title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    // 2. Перехватываем клик на самой картинке (без ripple), чтобы диалог не закрывался
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /* ничего не делаем, просто поглощаем клик */ }
                    )
                    // 3. Обработка жестов (зум и панорамирование)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan: Offset, zoom: Float, _ ->
                            scale = (scale * zoom).coerceIn(1f, 4f)
                            if (scale == 1f) {
                                offset = Offset.Zero
                            } else {
                                offset += pan
                            }
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            )

            // Кнопка закрытия
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text(
                    text = "✕",
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
private fun PositionRow(position: PositionEntity) {
    val context = LocalContext.current
    val imageRes = remember(position.imageName) {
        context.resources.getIdentifier(position.imageName, "drawable", context.packageName)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkBackground)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageRes != 0) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = position.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = position.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = position.callout,
                fontSize = 12.sp,
                color = TextSecondary
            )
            Text(
                text = position.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}
