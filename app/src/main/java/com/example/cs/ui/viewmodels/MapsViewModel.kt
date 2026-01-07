package com.example.cs.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs.R
import com.example.cs.data.db.DatabaseProvider
import com.example.cs.data.models.MapInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapsViewModel(application: Application) : AndroidViewModel(application) {

    private val _maps = MutableStateFlow<List<MapInfo>>(emptyList())
    val maps: StateFlow<List<MapInfo>> = _maps.asStateFlow()

    private val mapDefs = listOf(
        MapInfo("mirage",  "MIRAGE",  0, imageRes = R.drawable.map_mirage),
        MapInfo("dust2",   "DUST2",   0, imageRes = R.drawable.map_dust2),
        MapInfo("inferno", "INFERNO", 0, imageRes = R.drawable.map_inferno),
        MapInfo("nuke",    "NUKE",    0, imageRes = R.drawable.map_nuke),
        MapInfo("overpass","OVERPASS",0, imageRes = R.drawable.map_overpass),
        MapInfo("ancient", "ANCIENT", 0, imageRes = R.drawable.map_ancient),
        MapInfo("train",   "TRAIN",   0, imageRes = R.drawable.map_train),
        MapInfo("anubis",  "ANUBIS",  0, imageRes = R.drawable.map_anubis)
    )

    init {
        loadMaps()
    }

    private fun loadMaps() {
        viewModelScope.launch(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(getApplication())
            val dao = db.nadeDao()

            // один запрос на counts по всем mapId
            val countsByMap = dao.getCountsByMap().associate { it.mapId to it.count }

            _maps.value = mapDefs.map { m ->
                m.copy(nadesCount = countsByMap[m.id] ?: 0)
            }
        }
    }
}
