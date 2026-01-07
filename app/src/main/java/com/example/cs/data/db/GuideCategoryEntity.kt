package com.example.cs.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guide_categories")
data class GuideCategoryEntity(
    @PrimaryKey val id: String,   // "crosshairs", "graphics", "practice_maps"
    val title: String,
    val description: String
)
