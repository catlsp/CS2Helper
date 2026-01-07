package com.example.cs.data.repository

import com.example.cs.R
import com.example.cs.data.models.Category
import com.example.cs.data.models.MapInfo

class MapsRepository {

    fun getCategories(): List<Category> {
        return listOf(
            Category(
                id = "smokes",
                title = "Раскидки гранат",
                description = "Смоки, флешки и молотовы для популярных карт",
                icon = ""
            ),
            Category(
                id = "positions",
                title = "Позиции на картах",
                description = "Названия позиций",
                icon = ""
            ),
            Category(
                id = "guides",
                title = "Гайды для новичков",
                description = "Прицел, сенса, базовые советы",
                icon = ""
            )
        )
    }

    fun getMaps(): List<MapInfo> {
        return listOf(
            MapInfo(id = "mirage",  name = "MIRAGE",  nadesCount = 0, imageRes = R.drawable.map_mirage),
            MapInfo(id = "dust2",   name = "DUST2",   nadesCount = 0, imageRes = R.drawable.map_dust2),
            MapInfo(id = "inferno", name = "INFERNO", nadesCount = 0, imageRes = R.drawable.map_inferno),
            MapInfo(id = "nuke",    name = "NUKE",    nadesCount = 0, imageRes = R.drawable.map_nuke),
            MapInfo(id = "overpass",name = "OVERPASS",nadesCount = 0, imageRes = R.drawable.map_overpass),
            MapInfo(id = "ancient", name = "ANCIENT", nadesCount = 0, imageRes = R.drawable.map_ancient),
            MapInfo(id = "train",   name = "TRAIN",   nadesCount = 0, imageRes = R.drawable.map_train),
            MapInfo(id = "anubis",  name = "ANUBIS",  nadesCount = 0, imageRes = R.drawable.map_anubis)
        )
    }
}
