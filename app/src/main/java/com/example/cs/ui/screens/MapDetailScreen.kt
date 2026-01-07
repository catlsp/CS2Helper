package com.example.cs.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs.data.db.DatabaseProvider
import com.example.cs.data.db.NadeEntity
import com.example.cs.data.models.Nade
import com.example.cs.data.models.NadeImages
import com.example.cs.data.models.NadeType
import com.example.cs.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.defaultMinSize
import com.google.accompanist.flowlayout.FlowRow
import androidx.lifecycle.viewModelScope

class MapDetailViewModel(
    private val mapId: String,
    private val dbProvider: () -> com.example.cs.data.db.AppDatabase
) : ViewModel() {

    private val _nades = MutableStateFlow<List<Nade>>(emptyList())
    val nades: StateFlow<List<Nade>> = _nades.asStateFlow()

    private val _selectedSide = MutableStateFlow<String?>(null)
    val selectedSide: StateFlow<String?> = _selectedSide.asStateFlow()

    private val _selectedType = MutableStateFlow<NadeType?>(null)
    val selectedType: StateFlow<NadeType?> = _selectedType.asStateFlow()

    private var allNades: List<Nade> = emptyList()

    init {
        loadNadesFromDb()
    }

    private fun loadNadesFromDb() {
        // корутина во viewModelScope, чтобы не плодить свои scope
        viewModelScope.launch(Dispatchers.IO) {
            val db = dbProvider()
            val entities = db.nadeDao().getNadesForMap(mapId)
            val mapped = entities.map { it.toUiModel() }
            allNades = mapped
            _nades.value = mapped
        }
    }

    fun filterBySide(side: String?) {
        _selectedSide.value = side
        applyFilters()
    }

    fun filterByType(type: NadeType?) {
        _selectedType.value = type
        applyFilters()
    }

    private fun applyFilters() {
        val side = _selectedSide.value
        val type = _selectedType.value

        var filtered = allNades

        if (side != null) {
            filtered = filtered.filter { it.side == side }
        }
        if (type != null) {
            filtered = filtered.filter { it.type == type }
        }

        _nades.value = filtered
    }

    private fun NadeEntity.toUiModel(): Nade =
        Nade(
            id = id,
            mapId = mapId,
            title = title,
            side = side,
            type = NadeType.valueOf(type),
            throwType = com.example.cs.data.models.ThrowType.valueOf(throwType),
            difficulty = difficulty,
            position = position,
            aim = aim,
            description = description,
            landingSpot = landingSpot,
            images = NadeImages(
                position = imagePosition.orEmpty(),
                aim = imageAim.orEmpty(),
                result = imageResult.orEmpty()
            )
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDetailScreen(
    mapId: String,
    onBackClick: () -> Unit,
    onNadeClick: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: MapDetailViewModel = viewModel(
        key = mapId,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MapDetailViewModel(
                    mapId = mapId,
                    dbProvider = { DatabaseProvider.getDatabase(context) }
                ) as T
            }
        }
    )

    val nades by viewModel.nades.collectAsState()
    val selectedSide by viewModel.selectedSide.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        mapId.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Сторона T/CT
            FilterChips(
                title = "СТОРОНА",
                options = listOf("T", "CT"),
                selectedOption = selectedSide,
                onOptionSelected = { viewModel.filterBySide(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Тип гранаты
            NadeTypeFilters(
                selectedType = selectedType,
                onTypeSelected = { viewModel.filterByType(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (nades.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Нет раскидок для этих фильтров",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(nades) { nade ->
                        NadeCard(
                            nade = nade,
                            onClick = { onNadeClick(nade.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun FilterChips(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                text = "Все",
                isSelected = selectedOption == null,
                onClick = { onOptionSelected(null) }
            )

            options.forEach { option ->
                FilterChip(
                    text = option,
                    isSelected = selectedOption == option,
                    onClick = {
                        if (selectedOption == option) {
                            onOptionSelected(null)
                        } else {
                            onOptionSelected(option)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .defaultMinSize(minWidth = 50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) AccentOrange else CardBackground
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else TextSecondary
        )
    }
}


@Composable
fun NadeTypeFilters(
    selectedType: NadeType?,
    onTypeSelected: (NadeType?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "ТИП ГРАНАТЫ",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "★ — сложность исполнения раскидки.",
            fontSize = 10.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
        )


        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp
        ) {
            FilterChip(
                text = "Все",
                isSelected = selectedType == null,
                onClick = { onTypeSelected(null) }
            )

            NadeType.entries.forEach { type ->
                val label = when (type) {
                    NadeType.SMOKE -> "Smoke"
                    NadeType.FLASH -> "Flash"
                    NadeType.MOLOTOV -> "Molotov"
                    NadeType.HE -> "HE"
                }

                FilterChip(
                    text = label,
                    isSelected = selectedType == type,
                    onClick = {
                        if (selectedType == type) {
                            onTypeSelected(null)
                        } else {
                            onTypeSelected(type)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NadeCard(
    nade: Nade,
    onClick: () -> Unit
) {
    val sideColor = if (nade.side == "T") Color(0xFFFFB800) else AccentBlue

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                sideColor,
                                sideColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = nade.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(sideColor.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = nade.side,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = sideColor
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "★".repeat(nade.difficulty),
                        fontSize = 12.sp,
                        color = AccentOrange
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = nade.description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(sideColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "→",
                    fontSize = 18.sp,
                    color = sideColor
                )
            }
        }
    }
}
