package com.example.cs.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.example.cs.ui.game2048.Game2048Prefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun Game2048Dialog(onClose: () -> Unit) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0F1218)
        ) {
            Game2048Screen(onClose = onClose)
        }
    }
}

private enum class Dir { LEFT, RIGHT, UP, DOWN }

private data class TileState(
    val id: Long,
    val value: Int,
    val row: Int,
    val col: Int,
    val isNew: Boolean = false,
    val merged: Boolean = false,
    val remove: Boolean = false
)

private data class MoveTilesResult(
    val tiles: List<TileState>,
    val gained: Int,
    val changed: Boolean
)

@Composable
private fun Game2048Screen(onClose: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { Game2048Prefs(context) }
    val scope = rememberCoroutineScope()

    val bestScore by prefs.bestScoreFlow.collectAsState(initial = 0)

    var score by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }

    // Новые флаги для победы
    var hasWon by remember { mutableStateOf(false) }     // Достиг ли 2048
    var keepPlaying by remember { mutableStateOf(false) } // Нажал ли "Продолжить"

    var tiles by remember { mutableStateOf<List<TileState>>(emptyList()) }
    var nextId by remember { mutableLongStateOf(1L) }

    var animating by remember { mutableStateOf(false) }

    val moveAnimMs = 140L
    val popAnimMs = 110L

    fun boardFromTiles(list: List<TileState>): IntArray {
        val b = IntArray(16) { 0 }
        list.filter { !it.remove }.forEach { t ->
            b[t.row * 4 + t.col] = t.value
        }
        return b
    }

    fun canMove(b: IntArray): Boolean {
        if (b.any { it == 0 }) return true
        for (r in 0 until 4) {
            for (c in 0 until 4) {
                val v = b[r * 4 + c]
                if (c < 3 && v == b[r * 4 + (c + 1)]) return true
                if (r < 3 && v == b[(r + 1) * 4 + c]) return true
            }
        }
        return false
    }

    fun addRandomTile(list: List<TileState>, markNew: Boolean): List<TileState> {
        val occ = Array(4) { BooleanArray(4) }
        list.filter { !it.remove }.forEach { occ[it.row][it.col] = true }

        val empties = IntArray(16)
        var count = 0
        for (r in 0 until 4) for (c in 0 until 4) {
            if (!occ[r][c]) {
                empties[count] = r * 4 + c
                count++
            }
        }
        if (count == 0) return list

        val idx = empties[Random.nextInt(count)]
        val r = idx / 4
        val c = idx % 4
        val value = if (Random.nextFloat() < 0.9f) 2 else 4

        val tile = TileState(
            id = nextId++,
            value = value,
            row = r,
            col = c,
            isNew = markNew
        )
        return list + tile
    }

    fun resetGame() {
        score = 0
        gameOver = false
        hasWon = false
        keepPlaying = false
        animating = false
        tiles = emptyList()
        tiles = addRandomTile(tiles, markNew = false)
        tiles = addRandomTile(tiles, markNew = false)
    }

    LaunchedEffect(Unit) { resetGame() }

    LaunchedEffect(score) {
        if (score > bestScore) scope.launch { prefs.setBestScore(score) }
    }

    fun applyMoveTiles(current: List<TileState>, dir: Dir): MoveTilesResult {
        val byLine = Array(4) { mutableListOf<TileState>() }

        fun lineIndex(t: TileState): Int = when (dir) {
            Dir.LEFT, Dir.RIGHT -> t.row
            Dir.UP, Dir.DOWN -> t.col
        }

        fun orderKey(t: TileState): Int = when (dir) {
            Dir.LEFT -> t.col
            Dir.RIGHT -> 3 - t.col
            Dir.UP -> t.row
            Dir.DOWN -> 3 - t.row
        }

        current.filter { !it.remove }.forEach { t ->
            byLine[lineIndex(t)].add(t)
        }

        val out = mutableListOf<TileState>()
        var gained = 0
        var changed = false

        fun targetPos(line: Int, target: Int): Pair<Int, Int> = when (dir) {
            Dir.LEFT -> line to target
            Dir.RIGHT -> line to (3 - target)
            Dir.UP -> target to line
            Dir.DOWN -> (3 - target) to line
        }

        for (line in 0 until 4) {
            val lineTiles = byLine[line].sortedBy { orderKey(it) }
            var target = 0
            var last: TileState? = null
            var lastMerged = false

            for (t in lineTiles) {
                if (last != null && !lastMerged && last.value == t.value) {
                    val (tr, tc) = targetPos(line, target - 1)
                    val newValue = last.value * 2
                    gained += newValue

                    out.removeAt(out.lastIndex)
                    out.add(
                        last.copy(
                            value = newValue,
                            row = tr,
                            col = tc,
                            merged = true,
                            remove = false,
                            isNew = false
                        )
                    )
                    out.add(
                        t.copy(
                            row = tr,
                            col = tc,
                            remove = true,
                            merged = false,
                            isNew = false
                        )
                    )

                    changed = true
                    last = out[out.size - 2]
                    lastMerged = true
                } else {
                    val (tr, tc) = targetPos(line, target)
                    if (t.row != tr || t.col != tc) changed = true
                    out.add(
                        t.copy(
                            row = tr,
                            col = tc,
                            remove = false,
                            merged = false,
                            isNew = false
                        )
                    )
                    last = out.last()
                    lastMerged = false
                    target++
                }
            }
        }

        if (!changed) return MoveTilesResult(current, 0, changed = false)
        return MoveTilesResult(out, gained, changed = true)
    }

    fun move(dir: Dir) {
        // Если проиграли или идет анимация — стоп
        // Если выиграли (2048) и НЕ нажали "продолжить" — стоп (ждем реакции игрока)
        if (gameOver || animating || (hasWon && !keepPlaying)) return

        val res = applyMoveTiles(tiles, dir)
        if (!res.changed) return

        animating = true
        score += res.gained
        tiles = res.tiles

        scope.launch {
            delay(moveAnimMs)

            // Проверяем победу (появилась ли плитка 2048)
            // Важно проверять только живые плитки
            if (!hasWon && !keepPlaying) {
                val maxTile = tiles.filter { !it.remove }.maxOfOrNull { it.value } ?: 0
                if (maxTile >= 2048) {
                    hasWon = true
                    // Не ставим animating = false сразу, чтобы игрок увидел результат,
                    // но управление блокируется условием в начале move()
                }
            }

            tiles = tiles
                .filter { !it.remove }
                .map { it.copy(merged = false, isNew = false) }

            val beforeSpawnBoard = boardFromTiles(tiles)
            if (beforeSpawnBoard.any { it == 0 }) {
                tiles = addRandomTile(tiles, markNew = true)
                delay(popAnimMs)
                tiles = tiles.map { it.copy(isNew = false) }
            }

            val b = boardFromTiles(tiles)
            // Если мы выиграли прямо сейчас, gameOver пока не ставим, ждем решения игрока
            if (!hasWon) {
                gameOver = !canMove(b)
            } else if (keepPlaying) {
                // Если продолжаем играть, проверяем на проигрыш
                gameOver = !canMove(b)
            }

            animating = false
        }
    }

    val latestMove by rememberUpdatedState(newValue = { d: Dir -> move(d) })

    var dragX by remember { mutableStateOf(0f) }
    var dragY by remember { mutableStateOf(0f) }
    val swipeThreshold = 50f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("2048", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFB800))
            Spacer(Modifier.weight(1f))
            Text("Очки: $score", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(12.dp))
            Text("Рекорд: $bestScore", color = Color(0xFFB0B7C3))
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { resetGame() },
                modifier = Modifier.weight(1f)
            ) { Text("Заново") } // Переименовал для краткости

            OutlinedButton(
                onClick = onClose,
                modifier = Modifier.weight(1f)
            ) { Text("Выход") }
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1A1F29))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            dragX = 0f
                            dragY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragX += dragAmount.x
                            dragY += dragAmount.y
                        },
                        onDragEnd = {
                            // Блокируем свайпы, если выиграли и висит диалог
                            if (gameOver || animating || (hasWon && !keepPlaying)) return@detectDragGestures

                            if (abs(dragX) < swipeThreshold && abs(dragY) < swipeThreshold) return@detectDragGestures

                            if (abs(dragX) > abs(dragY)) {
                                if (dragX > 0) latestMove(Dir.RIGHT) else latestMove(Dir.LEFT)
                            } else {
                                if (dragY > 0) latestMove(Dir.DOWN) else latestMove(Dir.UP)
                            }
                        }
                    )
                }
                .padding(12.dp)
        ) {
            BoardArea(
                tiles = tiles,
                moveAnimStiffness = Spring.StiffnessMediumLow
            )

            // Оверлей проигрыша
            if (gameOver) {
                OverlayMessage(
                    title = "GAME OVER",
                    buttonText = "Попробовать снова",
                    onClick = { resetGame() }
                )
            }

            // Оверлей победы
            if (hasWon && !keepPlaying) {
                OverlayMessage(
                    title = "ПОБЕДА!",
                    titleColor = Color(0xFFFFB800),
                    buttonText = "Продолжить",
                    onClick = { keepPlaying = true }
                )
            }
        }
    }
}

// Вынес в отдельный компонент для переиспользования
@Composable
private fun OverlayMessage(
    title: String,
    titleColor: Color = Color.White,
    buttonText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .zIndex(10f), // Чтобы точно было поверх всего
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = titleColor,
                fontWeight = FontWeight.Black,
                fontSize = 32.sp
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB800))
            ) {
                Text(buttonText, color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun BoardArea(
    tiles: List<TileState>,
    moveAnimStiffness: Float
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val spacing = 10.dp
        val cell = (maxWidth - spacing * 3) / 4

        // фоновые "ячейки"
        for (r in 0 until 4) {
            for (c in 0 until 4) {
                val step = cell + spacing
                val x = step * c
                val y = step * r
                Box(
                    modifier = Modifier
                        .offset(x = x, y = y)
                        .size(cell)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF2A3242))
                )
            }
        }

        // Плитки поверх
        tiles.forEach { t ->
            key(t.id) {
                MovingTile(
                    tile = t,
                    cell = cell,
                    spacing = spacing,
                    moveAnimStiffness = moveAnimStiffness
                )
            }
        }
    }
}

@Composable
private fun MovingTile(
    tile: TileState,
    cell: Dp,
    spacing: Dp,
    moveAnimStiffness: Float
) {
    val step = cell + spacing
    val targetX = step * tile.col
    val targetY = step * tile.row

    val x by animateDpAsState(
        targetValue = targetX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = moveAnimStiffness
        ),
        label = "tileX"
    )
    val y by animateDpAsState(
        targetValue = targetY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = moveAnimStiffness
        ),
        label = "tileY"
    )

    val alpha by animateFloatAsState(
        targetValue = if (tile.remove) 0f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium),
        label = "tileAlpha"
    )

    val scale by animateFloatAsState(
        targetValue = when {
            tile.isNew -> 1.08f
            tile.merged -> 1.10f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "tileScale"
    )

    val bg = tileColor(tile.value)
    val textColor = if (tile.value <= 4) Color(0xFF3C3A32) else Color.White

    Box(
        modifier = Modifier
            .zIndex(if (tile.remove) 2f else 1f)
            .offset(x = x, y = y)
            .size(cell)
            .scale(scale)
            .alpha(alpha)
            .clip(RoundedCornerShape(12.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        if (tile.value != 0) {
            Text(
                text = tile.value.toString(),
                color = textColor,
                fontWeight = FontWeight.Black,
                fontSize = if (tile.value < 128) 22.sp else 18.sp
            )
        }
    }
}

private fun tileColor(value: Int): Color = when (value) {
    0 -> Color(0xFF2A3242)
    2 -> Color(0xFFEEE4DA)
    4 -> Color(0xFFEDE0C8)
    8 -> Color(0xFFF2B179)
    16 -> Color(0xFFF59563)
    32 -> Color(0xFFF67C5F)
    64 -> Color(0xFFF65E3B)
    128 -> Color(0xFFEDCF72)
    256 -> Color(0xFFEDCC61)
    512 -> Color(0xFFEDC850)
    1024 -> Color(0xFFEDC53F)
    2048 -> Color(0xFFEDC22E)
    else -> Color(0xFF3C3A32) // Для 4096 и больше - темный
}
