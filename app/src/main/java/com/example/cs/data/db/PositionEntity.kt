package com.example.cs.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "positions")
data class PositionEntity(
    @PrimaryKey val id: String,
    val mapId: String,
    val name: String,
    val callout: String,
    val description: String,
    val imageName: String
)
