package com.example.cs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource // Важный импорт!
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset // Важный импорт для жестов!
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
import com.example.cs.data.models.Nade
import com.example.cs.data.models.ThrowType
import com.example.cs.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NadeDetailScreen(
    nade: Nade,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var fullscreenImageId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            nade.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            nade.mapId.uppercase(),
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val sideColor = if (nade.side == "T") Color(0xFFFFB800) else AccentBlue
                Badge(nade.side, sideColor)
                Badge(nade.type.name, CategorySmokes)
                Badge("★".repeat(nade.difficulty), AccentOrange)
            }

            InfoCard(title = "Описание", content = nade.description)

            StepCard(
                stepNumber = 1,
                title = "Где встать",
                content = nade.position,
                imageName = nade.images?.position,
                onImageClick = { id -> fullscreenImageId = id }
            )

            StepCard(
                stepNumber = 2,
                title = "Куда целиться",
                content = nade.aim,
                imageName = nade.images?.aim,
                onImageClick = { id -> fullscreenImageId = id }
            )

            ThrowTypeCard(nade.throwType)

            ResultCard(
                landingSpot = nade.landingSpot,
                resultImageName = nade.images?.result,
                onImageClick = { id -> fullscreenImageId = id }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (fullscreenImageId != null) {
        ZoomableImageDialog(
            imageId = fullscreenImageId!!,
            onDismiss = { fullscreenImageId = null }
        )
    }
}

@Composable
fun ZoomableImageDialog(
    imageId: Int,
    onDismiss: () -> Unit
) {
    // Используем remember, чтобы не создавать новый объект при каждой рекомпозиции
    val interactionSource = remember { MutableInteractionSource() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            var scale by remember { mutableStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }

            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    // Перехватываем клики на картинке
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { }
                    )
                    .pointerInput(Unit) {
                        // Явное указание типов параметров: centroid, pan, zoom, rotation
                        detectTransformGestures { _, pan: Offset, zoom: Float, _ ->
                            scale = (scale * zoom).coerceIn(1f, 4f)
                            if (scale == 1f) {
                                offset = Offset.Zero
                            } else {
                                val newOffset = offset + pan
                                offset = newOffset
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

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text("✕", color = Color.White, fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun Badge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun InfoCard(
    title: String,
    content: String,
    color: Color = CardBackground
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AccentOrange,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun StepCard(
    stepNumber: Int,
    title: String,
    content: String,
    imageName: String?,
    onImageClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(AccentOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            StepImage(title = title, imageName = imageName, onClick = onImageClick)
            Text(
                text = content,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun StepImage(
    title: String,
    imageName: String?,
    onClick: (Int) -> Unit
) {
    if (imageName.isNullOrBlank()) return

    val context = LocalContext.current
    val imageId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    if (imageId != 0) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick(imageId) },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))
    } else {
        PlaceholderImage("$title (не найдено: $imageName)")
    }
}

@Composable
fun PlaceholderImage(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Картинка: $label",
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun ThrowTypeCard(throwType: ThrowType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(AccentBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "3",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Как бросать",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            val label = when (throwType) {
                ThrowType.LEFT_CLICK -> "Обычный бросок (ЛКМ)"
                ThrowType.RIGHT_CLICK -> "Подброс (ПКМ)"
                ThrowType.JUMP_THROW -> "Прыжок-бросок (ЛКМ + Jump)"
                ThrowType.LEFT_RIGHT_CLICK -> "Средний бросок (ЛКМ + ПКМ)"
            }
            Text(
                text = label,
                fontSize = 14.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ResultCard(
    landingSpot: String,
    resultImageName: String?,
    onImageClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AccentOrange.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Результат",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AccentOrange
            )
            Spacer(modifier = Modifier.height(12.dp))
            StepImage(title = "Результат", imageName = resultImageName, onClick = onImageClick)
            Text(
                text = landingSpot,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp
            )
        }
    }
}
