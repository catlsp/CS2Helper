package com.example.cs.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nades")
data class NadeEntity(
    @PrimaryKey val id: String,
    val mapId: String,
    val title: String,
    val side: String,          // "T" или "CT"
    val type: String,          // "SMOKE", "FLASH", ...
    val throwType: String,     // "LEFT_CLICK", ...
    val difficulty: Int,
    val position: String,
    val aim: String,
    val description: String,
    val landingSpot: String,
    val imagePosition: String?, // имя drawable
    val imageAim: String?,      // "mirage_ct_aim"
    val imageResult: String?    // "mirage_ct_result"
)
