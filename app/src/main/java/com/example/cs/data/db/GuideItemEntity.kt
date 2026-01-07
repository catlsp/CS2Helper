package com.example.cs.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guide_items")
data class GuideItemEntity(
    @PrimaryKey val id: String,
    val categoryId: String,      // "crosshairs", "graphics", "practice_maps"
    val title: String,
    val description: String,
    val extra: String? = null,   // код прицела / команды текста
    val url: String? = null,     // ссылка на Workshop / страницу
    val type: String? = null     // "crosshair", "map", "text" и т.п.
)
