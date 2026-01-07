package com.example.cs.data.db

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object PositionInitializer {

    fun initPositionsIfEmpty(db: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = db.positionDao()

            // ВАЖНО: не выходим по "mirage", иначе остальные карты не добавятся. [web:186]
            val positions = mutableListOf<PositionEntity>()

            // ================= MIRAGE =================
            positions += listOf(
                PositionEntity(
                    id = "mirage_ticket",
                    mapId = "mirage",
                    name = "Ticket",
                    callout = "Ticket Booth",
                    description = "Задняя позиция CT на A, часто позиция AWP.",
                    imageName = "mirage_ticket" // если нет файла mirage_ticket.jpg -> можно поставить ""
                ),
                PositionEntity(
                    id = "mirage_default",
                    mapId = "mirage",
                    name = "Default",
                    callout = "Default Plant",
                    description = "Стандартное место установки бомбы на A.",
                    imageName = "mirage_default"
                ),
                PositionEntity(
                    id = "mirage_jungle",
                    mapId = "mirage",
                    name = "Jungle",
                    callout = "Jungle",
                    description = "Переход между mid и A, ключевая точка обороны.",
                    imageName = "mirage_jungle_pos"
                ),
                PositionEntity(
                    id = "mirage_ct",
                    mapId = "mirage",
                    name = "CT",
                    callout = "CT Spawn",
                    description = "КТ-спавн и выход на А через тикет/джангл.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "mirage_palace",
                    mapId = "mirage",
                    name = "Palace",
                    callout = "Palace",
                    description = "Выход террористов на A через палас.",
                    imageName = ""
                )
            )

            // ================= DUST2 =================
            positions += listOf(
                PositionEntity(
                    id = "dust2_long",
                    mapId = "dust2",
                    name = "Long",
                    callout = "Long",
                    description = "Длинная на A, ключевой контроль карты.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "dust2_short",
                    mapId = "dust2",
                    name = "Short",
                    callout = "Catwalk",
                    description = "Короткая на A (кат).",
                    imageName = ""
                ),
                PositionEntity(
                    id = "dust2_mid",
                    mapId = "dust2",
                    name = "Mid",
                    callout = "Mid",
                    description = "Центр карты, выходы на short/B.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "dust2_xbox",
                    mapId = "dust2",
                    name = "Xbox",
                    callout = "Xbox",
                    description = "Ящик в центре mid.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "dust2_b_tunnels",
                    mapId = "dust2",
                    name = "Tunnels",
                    callout = "Upper/Lower Tunnels",
                    description = "Тоннели к B.",
                    imageName = ""
                )
            )

            // ================= INFERNO =================
            positions += listOf(
                PositionEntity(
                    id = "inferno_banana",
                    mapId = "inferno",
                    name = "Banana",
                    callout = "Banana",
                    description = "Дорога к B, частая зона контакта.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "inferno_coffins",
                    mapId = "inferno",
                    name = "Coffins",
                    callout = "Coffins",
                    description = "Позиция защиты B за гробами.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "inferno_pit",
                    mapId = "inferno",
                    name = "Pit",
                    callout = "Pit",
                    description = "Яма на A, сильная позиция защиты.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "inferno_apps",
                    mapId = "inferno",
                    name = "Apps",
                    callout = "Apartments",
                    description = "Ковры/апсы к A.",
                    imageName = ""
                )
            )

            // ================= NUKE =================
            positions += listOf(
                PositionEntity(
                    id = "nuke_outside",
                    mapId = "nuke",
                    name = "Outside",
                    callout = "Outside",
                    description = "Улица, контроль проходов к Secret/Garage.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "nuke_secret",
                    mapId = "nuke",
                    name = "Secret",
                    callout = "Secret",
                    description = "Спуск к нижнему пленту (B).",
                    imageName = ""
                ),
                PositionEntity(
                    id = "nuke_ramp",
                    mapId = "nuke",
                    name = "Ramp",
                    callout = "Ramp",
                    description = "Рампа — ключ к выходу на нижний плент.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "nuke_hut",
                    mapId = "nuke",
                    name = "Hut",
                    callout = "Hut",
                    description = "Домик на A (hut).",
                    imageName = ""
                )
            )

            // ================= OVERPASS =================
            positions += listOf(
                PositionEntity(
                    id = "overpass_monster",
                    mapId = "overpass",
                    name = "Monster",
                    callout = "Monster",
                    description = "Основной вход на B со стороны T.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "overpass_short_b",
                    mapId = "overpass",
                    name = "Short B",
                    callout = "Short",
                    description = "Короткий на B (выход из коннектора).",
                    imageName = ""
                ),
                PositionEntity(
                    id = "overpass_toilets",
                    mapId = "overpass",
                    name = "Toilets",
                    callout = "Toilets",
                    description = "Туалеты возле A.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "overpass_bank",
                    mapId = "overpass",
                    name = "Bank",
                    callout = "Bank",
                    description = "Банк — часть A/retake зоны.",
                    imageName = ""
                )
            )

            // ================= ANCIENT =================
            positions += listOf(
                PositionEntity(
                    id = "ancient_mid",
                    mapId = "ancient",
                    name = "Mid",
                    callout = "Mid",
                    description = "Центр Ancient, контроль ротаций.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "ancient_donut",
                    mapId = "ancient",
                    name = "Donut",
                    callout = "Donut",
                    description = "Переход к A через donut.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "ancient_cave",
                    mapId = "ancient",
                    name = "Cave",
                    callout = "Cave",
                    description = "Проход к B (часто называют cave).",
                    imageName = ""
                )
            )

            // ================= TRAIN =================
            positions += listOf(
                PositionEntity(
                    id = "train_ivy",
                    mapId = "train",
                    name = "Ivy",
                    callout = "Ivy",
                    description = "Проход к A через ivy.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "train_popdog",
                    mapId = "train",
                    name = "Popdog",
                    callout = "Popdog",
                    description = "Люк/лестница (popdog).",
                    imageName = ""
                ),
                PositionEntity(
                    id = "train_b_upper",
                    mapId = "train",
                    name = "Upper B",
                    callout = "Upper",
                    description = "Верхний заход на B.",
                    imageName = ""
                )
            )

            // ================= ANUBIS =================
            positions += listOf(
                PositionEntity(
                    id = "anubis_mid",
                    mapId = "anubis",
                    name = "Mid",
                    callout = "Mid",
                    description = "Центр Anubis, важная зона для контроля карты.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "anubis_connector",
                    mapId = "anubis",
                    name = "Connector",
                    callout = "Connector",
                    description = "Связка между mid и плентами.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "anubis_a_main",
                    mapId = "anubis",
                    name = "A Main",
                    callout = "A Main",
                    description = "Основной вход на A.",
                    imageName = ""
                ),
                PositionEntity(
                    id = "anubis_b_main",
                    mapId = "anubis",
                    name = "B Main",
                    callout = "B Main",
                    description = "Основной вход на B.",
                    imageName = ""
                )
            )

            dao.insertAll(positions) // REPLACE перезапишет по одинаковому id. [web:172]
        }
    }
}
