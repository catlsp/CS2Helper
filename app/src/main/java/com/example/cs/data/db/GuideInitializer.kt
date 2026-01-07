package com.example.cs.data.db

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object GuideInitializer {

    fun initGuidesIfEmpty(db: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = db.guideDao()
            val existing = dao.getCategories()
            if (existing.isNotEmpty()) return@launch

            val categories = listOf(
                GuideCategoryEntity(
                    id = "crosshairs",
                    title = "Популярные прицелы",
                    description = "Коды удобных и популярных прицелов"
                ),
                GuideCategoryEntity(
                    id = "graphics",
                    title = "Настройки графики",
                    description = "Лучший баланс FPS и качества"
                ),
                GuideCategoryEntity(
                    id = "practice_maps",
                    title = "Карты для тренировки",
                    description = "Aim, раскидки, реакция"
                )
            )

            val items = listOf(
                // ===== ПОПУЛЯРНЫЕ ПРИЦЕЛЫ (15 шт.) =====
                GuideItemEntity(
                    id = "crosshair_s1mple",
                    categoryId = "crosshairs",
                    title = "Прицел s1mple",
                    description = "Тонкий зелёный прицел, хорошо видно AWP и rifle.",
                    extra = "CSGO-SVwo4-7qUMA-DhTJf-5iesE-xtdqA",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_niko",
                    categoryId = "crosshairs",
                    title = "Прицел NiKo",
                    description = "Статичный белый прицел для стрельбы по головам.",
                    extra = "CSGO-bHVJu-haJnm-oeJtz-EFusJ-LEwzF",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_zywoo",
                    categoryId = "crosshairs",
                    title = "Прицел ZywOo",
                    description = "Небольшой зелёный прицел с минимальной толщиной.",
                    extra = "CSGO-4ENaA-AH2oJ-np6BN-EODuy-5RGOP",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_m0nesy",
                    categoryId = "crosshairs",
                    title = "Прицел m0NESY",
                    description = "Маленький жёлтый прицел для агрессивной игры.",
                    extra = "CSGO-GsnzG-rBZDa-ry2xe-zrqZs-qKwLA",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_s1mple_dot",
                    categoryId = "crosshairs",
                    title = "Точечный прицел (s1mple‑style)",
                    description = "Одна точка по центру — удобно тренировать контроль спрея.",
                    extra = "CSGO-d7Jxa-wkK3b-V8v7u-c47aE-YpZpQ",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_twistzz",
                    categoryId = "crosshairs",
                    title = "Прицел Twistzz",
                    description = "Классический голубой прицел среднего размера.",
                    extra = "CSGO-aD4sL-J2r7V-XQ9aC-PkK4M-fuGQH",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_b1t",
                    categoryId = "crosshairs",
                    title = "Прицел b1t",
                    description = "Очень маленький статичный прицел для точной стрельбы.",
                    extra = "CSGO-eD2Qp-tpXvC-O9bGf-7kV3P-dz9fN",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_ropz",
                    categoryId = "crosshairs",
                    title = "Прицел ropz",
                    description = "Тонкий бирюзовый прицел, хорошо заметен на большинстве карт.",
                    extra = "CSGO-2u9hE-uC7aP-2oXvR-p9k4x-hqCsL",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_electronic",
                    categoryId = "crosshairs",
                    title = "Прицел electroNic",
                    description = "Чуть шире стандартного, с небольшим gap для спрея.",
                    extra = "CSGO-MbA7H-4kO2s-KqQ8C-w7VX9-FtJbP",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_dev1ce",
                    categoryId = "crosshairs",
                    title = "Прицел dev1ce",
                    description = "Классический зелёный прицел под AWP и позиционную игру.",
                    extra = "CSGO-zC3aK-7Yo2p-Q9VbR-tQFzB-6ho2L",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_scream",
                    categoryId = "crosshairs",
                    title = "Прицел ScreaM",
                    description = "Маленький жёлтый прицел, ориентированный на one‑tap.",
                    extra = "CSGO-xA7pN-bQ9oX-3vTzP-KL5yC-4nQvB",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_simple_newbie",
                    categoryId = "crosshairs",
                    title = "Простой прицел для новичков",
                    description = "Зелёный статичный прицел среднего размера, удобен для старта.",
                    extra = "CSGO-dm3zQ-2P4kH-7bncX-Vq9JY-Hf5Ls",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_thick",
                    categoryId = "crosshairs",
                    title = "Толстый прицел",
                    description = "Подходит, если играешь на маленьком разрешении и плохо видишь тонкие линии.",
                    extra = "CSGO-c2vNQ-G7pLm-uz4Xt-8bKqJ-QfR5D",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_minimal",
                    categoryId = "crosshairs",
                    title = "Минималистичный прицел",
                    description = "Тонкий белый прицел с небольшим разрывом по центру.",
                    extra = "CSGO-qL9pE-bG2tX-M5v7J-WcK4P-8hRnB",
                    type = "crosshair"
                ),
                GuideItemEntity(
                    id = "crosshair_awp_focused",
                    categoryId = "crosshairs",
                    title = "Прицел под AWP",
                    description = "Тонкий зелёный прицел, комфортный при переходе на зум.",
                    extra = "CSGO-kP3qV-uZ7sN-4cLxE-9wFtB-hQ2rM",
                    type = "crosshair"
                ),

                // ===== НАСТРОЙКИ ГРАФИКИ =====
                GuideItemEntity(
                    id = "graphics_max_fps",
                    categoryId = "graphics",
                    title = "Максимальный FPS",
                    description = "Все настройки низкие, текстуры на средних. Подходит для слабых ПК.",
                    extra = null,
                    type = "text"
                ),
                GuideItemEntity(
                    id = "graphics_balance",
                    categoryId = "graphics",
                    title = "Баланс FPS и качества",
                    description = "Тени и текстуры на средних, остальное низкое. Комфортная картинка без сильной просадки FPS.",
                    extra = null,
                    type = "text"
                ),

                // ===== КАРТЫ ДЛЯ ТРЕНИРОВКИ (10 шт.) =====
                GuideItemEntity(
                    id = "practice_aim_botz",
                    categoryId = "practice_maps",
                    title = "Aim Botz (CS2)",
                    description = "Классическая карта для прогрева и аима против ботов.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=3070244462",
                    type = "map"
                ),
                GuideItemEntity(
                    id = "practice_recoil_master",
                    categoryId = "practice_maps",
                    title = "Recoil Master (CS2)",
                    description = "Тренировка спрея и контроля отдачи для всех автоматов.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=3100869952",
                    type = "map"
                ),
                GuideItemEntity(
                    id = "practice_yprac_hub",
                    categoryId = "practice_maps",
                    title = "Yprac Hub",
                    description = "Хаб Yesber с тренингами на разные карты и ситуации.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=3070715607",
                    type = "map"
                ),
                GuideItemEntity(
                    id = "practice_yprac_mirage",
                    categoryId = "practice_maps",
                    title = "Yprac Mirage",
                    description = "Сценарии для тренировки Mirage: выходы, ретейки, смоки.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=1222094548",
                    type = "map"
                ),
                GuideItemEntity(
                    id = "practice_yprac_inferno",
                    categoryId = "practice_maps",
                    title = "Yprac Inferno",
                    description = "Тренировка дуэлей и раскидок на Inferno.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=1222102355",
                    type = "map"
                ),
                GuideItemEntity(
                    id = "practice_yprac_aim",
                    categoryId = "practice_maps",
                    title = "Yprac Aim",
                    description = "Сценарии для аима: реакция, трекинг, флики.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=3115601938",
                    type = "map"
                ),
                GuideItemEntity(
                    id = "practice_fast_aim",
                    categoryId = "practice_maps",
                    title = "Fast Aim / Reflex",
                    description = "Быстрая тренировка реакции и перевода прицела по ботам.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=368026786",
                    type = "map"
                ),
                GuideItemEntity(
                    id = "practice_bots_training",
                    categoryId = "practice_maps",
                    title = "Training with Bots",
                    description = "Тренировка выхода на точки и ретейков с ботами.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=365126929",
                    type = "map"
                ),
                GuideItemEntity(
                    id = "practice_smoke_mirage",
                    categoryId = "practice_maps",
                    title = "Mirage Smokes",
                    description = "Карта для тренировки смоков и флешек на Mirage.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=308490450",
                    type = "map"
                ),
                GuideItemEntity(
                    id = "practice_flick_training",
                    categoryId = "practice_maps",
                    title = "Flick Training",
                    description = "Тренировка быстрых фликов, полезно для AWP.",
                    url = "https://steamcommunity.com/sharedfiles/filedetails/?id=368431521",
                    type = "map"
                )
            )

            dao.insertCategories(categories)
            dao.insertItems(items)
        }
    }
}
