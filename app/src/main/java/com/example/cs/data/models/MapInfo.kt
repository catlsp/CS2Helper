package com.example.cs.data.models

data class MapInfo(
    val id: String,
    val name: String,
    val nadesCount: Int,
    val imageRes: Int? = null
)

data class Category(
    val id: String,
    val title: String,
    val description: String,
    val icon: String
)

data class Nade(
    val id: String,
    val mapId: String,
    val title: String,
    val side: String,              // "T" или "CT"
    val type: NadeType,
    val throwType: ThrowType,
    val difficulty: Int,           // 1-3
    val position: String,          // где встать
    val aim: String,               // куда целиться
    val description: String,       // описание
    val landingSpot: String,       // куда попадёт граната
    val images: NadeImages? = null // <-- ДОБАВЬ ЭТО!
)

data class NadeImages(
    val position: String,  // имя drawable
    val aim: String,
    val result: String
)

enum class NadeType {
    SMOKE,
    FLASH,
    MOLOTOV,
    HE
}

enum class ThrowType {
    LEFT_CLICK,        // обычный бросок
    RIGHT_CLICK,       // подброс
    JUMP_THROW,        // прыжок-бросок
    LEFT_RIGHT_CLICK   // средний бросок
}

data class Position(
    val id: String,
    val mapId: String,
    val name: String,        // название позиции: Ticket, Default, Short и т.п.
    val callout: String,     // как её обычно называют
    val description: String, // кратко что это за точка
    val imageName: String    // имя drawable с фрагментом карты / меткой
)
