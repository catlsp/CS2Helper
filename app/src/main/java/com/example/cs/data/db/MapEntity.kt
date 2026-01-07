package com.example.cs.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maps")
data class MapEntity(
    @PrimaryKey val id: String,
    val name: String,
    val nadesCount: Int = 0
)
