package com.example.cs.data.db

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object NadeInitializer {

    fun initNadesIfEmpty(db: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = db.nadeDao()

            // ВАЖНО:
            // НЕ делаем "if (total > 0) return"
            // чтобы при разработке обновлялись поля (в том числе имена картинок) через REPLACE. [web:176][web:162]

            val nades = mutableListOf<NadeEntity>()

            // ================= MIRAGE =================
            nades += listOf(
                NadeEntity(
                    id = "mirage_ct_smoke",
                    mapId = "mirage",
                    title = "CT Smoke (From Ramp)",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 1,
                    position = "Встань в углу T‑рампы у стены.",
                    aim = "Прицелься в угол крыши над CT.",
                    description = "Базовый смок на CT спавн. Перекрывает обзор снайперов с CT.",
                    landingSpot = "CT спавн, блокирует обзор на A site и mid.",
                    imagePosition = "mirage_ct_position",
                    imageAim = "mirage_ct_aim",
                    imageResult = "mirage_ct_result"
                ),
                NadeEntity(
                    id = "mirage_jungle_smoke",
                    mapId = "mirage",
                    title = "Jungle Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "LEFT_CLICK",
                    difficulty = 2,
                    position = "Встань у стены в топ‑миде возле арки.",
                    aim = "Прицелься в левый верхний угол окна коннектора.",
                    description = "Смок на джангл с безопасной позиции. Идеален для выхода на A.",
                    landingSpot = "Полностью перекрывает джангл.",
                    imagePosition = "mirage_jungle_position",
                    imageAim = "mirage_jungle_aim",
                    imageResult = "mirage_jungle_result"
                ),
                NadeEntity(
                    id = "mirage_stairs_smoke",
                    mapId = "mirage",
                    title = "Stairs Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 2,
                    position = "Стань в T‑рампе около большого ящика.",
                    aim = "Целься в край антенны над коннектором.",
                    description = "Смок на лестницу (stairs), помогает безопасно зайти на A.",
                    landingSpot = "Лестница на точке A.",
                    imagePosition = "mirage_stairs_position",
                    imageAim = "mirage_stairs_aim",
                    imageResult = "mirage_stairs_result"
                ),
                NadeEntity(
                    id = "mirage_a_flash",
                    mapId = "mirage",
                    title = "A Site Flash",
                    side = "T",
                    type = "FLASH",
                    throwType = "RIGHT_CLICK",
                    difficulty = 1,
                    position = "Встань у входа в palace.",
                    aim = "Целься в потолок над тетрисом.",
                    description = "Флешка на A. Ослепляет игроков на пленте и в jungle.",
                    landingSpot = "Взрывается над точкой A.",
                    imagePosition = "mirage_a_flash_position",
                    imageAim = "mirage_a_flash_aim",
                    imageResult = "mirage_a_flash_result"
                ),
                NadeEntity(
                    id = "mirage_molotov_default",
                    mapId = "mirage",
                    title = "Default Molotov",
                    side = "CT",
                    type = "MOLOTOV",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Встань на A site возле цветка (саженца).",
                    aim = "Целься в угол дефолтного ящика.",
                    description = "Молотов на дефолт. Выбивает террористов с позиции при пленте.",
                    landingSpot = "Default plant на A.",
                    imagePosition = "mirage_molotov_position",
                    imageAim = "mirage_molotov_aim",
                    imageResult = "mirage_molotov_result"
                )
            )

            // ================= DUST2 =================
            nades += listOf(
                NadeEntity(
                    id = "dust2_long_smoke",
                    mapId = "dust2",
                    title = "Long Corner Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 1,
                    position = "Встань у ящика на T‑спавне напротив выхода на Long.",
                    aim = "Наведи прицел чуть выше края дома у Long.",
                    description = "Смок на угол Long, чтобы занять длину без контакта с AWP.",
                    landingSpot = "Угол Long, перекрывает обзор CT.",
                    imagePosition = "dust2_long_smoke_pos",
                    imageAim = "dust2_long_smoke_aim",
                    imageResult = "dust2_long_smoke_res"
                ),
                NadeEntity(
                    id = "dust2_ct_smoke",
                    mapId = "dust2",
                    title = "CT Smoke from Spawn",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 2,
                    position = "Стань в углу T‑спавна у машины.",
                    aim = "Целься в край трубы над центром.",
                    description = "Смок на CT для выхода на B или mid‑to‑B.",
                    landingSpot = "CT спавн у кт‑рама.",
                    imagePosition = "dust2_ct_smoke_pos",
                    imageAim = "dust2_ct_smoke_aim",
                    imageResult = "dust2_ct_smoke_res"
                ),
                NadeEntity(
                    id = "dust2_lower_flash",
                    mapId = "dust2",
                    title = "Lower Tunnel Flash",
                    side = "T",
                    type = "FLASH",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Встань в тоннеле рядом с ящиками.",
                    aim = "Кинь флешку над входом в lower.",
                    description = "Флешка для агрессивного выхода из lower на mid.",
                    landingSpot = "Над выходом из lower, ослепляет mid.",
                    imagePosition = "dust2_lower_flash_pos",
                    imageAim = "dust2_lower_flash_aim",
                    imageResult = "dust2_lower_flash_res"
                ),
                NadeEntity(
                    id = "dust2_b_molotov",
                    mapId = "dust2",
                    title = "B Plat Molotov",
                    side = "T",
                    type = "MOLOTOV",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Стань в двери B, прижавшись к стене справа.",
                    aim = "Целься в угол платформы B.",
                    description = "Молотов выжигает платформу B, не давая опорнику пикать.",
                    landingSpot = "Платформа B site.",
                    imagePosition = "dust2_b_molo_pos",
                    imageAim = "dust2_b_molo_aim",
                    imageResult = "dust2_b_molo_res"
                )
            )

            // ================= INFERNO =================
            nades += listOf(
                NadeEntity(
                    id = "inferno_banana_smoke",
                    mapId = "inferno",
                    title = "Deep Banana Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 2,
                    position = "Встань у бочки на T‑стороне банана.",
                    aim = "Прицелься в верх трубы над стеной.",
                    description = "Глубокий смок на банан, отрезает опорника B.",
                    landingSpot = "Перед карманом на банане.",
                    imagePosition = "inferno_banana_smoke_pos",
                    imageAim = "inferno_banana_smoke_aim",
                    imageResult = "inferno_banana_smoke_res"
                ),
                NadeEntity(
                    id = "inferno_coffins_smoke",
                    mapId = "inferno",
                    title = "Coffins Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 1,
                    position = "Стань у машины на банане.",
                    aim = "Наведи прицел на угол здания над coffins.",
                    description = "Смок на coffins для выхода на B.",
                    landingSpot = "Coffins на B site.",
                    imagePosition = "inferno_coffins_pos",
                    imageAim = "inferno_coffins_aim",
                    imageResult = "inferno_coffins_res"
                ),
                NadeEntity(
                    id = "inferno_pit_molotov",
                    mapId = "inferno",
                    title = "Pit Molotov",
                    side = "T",
                    type = "MOLOTOV",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Встань у тетриса перед A site.",
                    aim = "Целься в угол дома над pit.",
                    description = "Молотов выжигает pit, не давая засейвиться опорнику.",
                    landingSpot = "Pit у точки A.",
                    imagePosition = "inferno_pit_molo_pos",
                    imageAim = "inferno_pit_molo_aim",
                    imageResult = "inferno_pit_molo_res"
                ),
                NadeEntity(
                    id = "inferno_ct_flash",
                    mapId = "inferno",
                    title = "CT Flash A",
                    side = "T",
                    type = "FLASH",
                    throwType = "RIGHT_CLICK",
                    difficulty = 1,
                    position = "Стань у середины mid перед аркой.",
                    aim = "Кинь флешку над аркой в сторону CT.",
                    description = "Флешка для выхода с mid на A, ослепляет CT и short.",
                    landingSpot = "Над аркой на mid.",
                    imagePosition = "inferno_ct_flash_pos",
                    imageAim = "inferno_ct_flash_aim",
                    imageResult = "inferno_ct_flash_res"
                )
            )

            // ================= NUKE =================
            nades += listOf(
                NadeEntity(
                    id = "nuke_outside_wall",
                    mapId = "nuke",
                    title = "Outside Wall Smokes",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 2,
                    position = "Встань на T‑спавне у красного ящика.",
                    aim = "Серия смоков: целься в метки на крыше над outside.",
                    description = "Линия смоков на улице, чтобы пройти к secret.",
                    landingSpot = "Стена смоков между red и secret.",
                    imagePosition = "nuke_wall_pos",
                    imageAim = "nuke_wall_aim",
                    imageResult = "nuke_wall_res"
                ),
                NadeEntity(
                    id = "nuke_main_smoke",
                    mapId = "nuke",
                    title = "Main Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 1,
                    position = "Встань у красного ящика на улице.",
                    aim = "Целься в угол крыши над main.",
                    description = "Смок на main для выхода на A сверху/с улицы.",
                    landingSpot = "Вход в main.",
                    imagePosition = "nuke_main_smoke_pos",
                    imageAim = "nuke_main_smoke_aim",
                    imageResult = "nuke_main_smoke_res"
                ),
                NadeEntity(
                    id = "nuke_hut_molotov",
                    mapId = "nuke",
                    title = "Hut Molotov",
                    side = "T",
                    type = "MOLOTOV",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Стань перед дверью A site.",
                    aim = "Кинь молотов в дверь hut.",
                    description = "Молотов выжигает hut, мешая опорнику играть в упоре.",
                    landingSpot = "Внутри hut.",
                    imagePosition = "nuke_hut_molo_pos",
                    imageAim = "nuke_hut_molo_aim",
                    imageResult = "nuke_hut_molo_res"
                ),
                NadeEntity(
                    id = "nuke_ramp_flash",
                    mapId = "nuke",
                    title = "Ramp Popflash",
                    side = "CT",
                    type = "FLASH",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Стань на рампе у радио‑комнаты.",
                    aim = "Кинь флешку над входом на рампу.",
                    description = "Pop‑флешка для ретейка или агрессии по рампе.",
                    landingSpot = "Взрывается над рампой.",
                    imagePosition = "nuke_ramp_flash_pos",
                    imageAim = "nuke_ramp_flash_aim",
                    imageResult = "nuke_ramp_flash_res"
                )
            )

            // ================= OVERPASS =================
            nades += listOf(
                NadeEntity(
                    id = "overpass_a_smoke",
                    mapId = "overpass",
                    title = "Bank & Truck Smokes",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 2,
                    position = "Встань на ступеньках к A из туалетов.",
                    aim = "Две разные точки на крышах банка и truck.",
                    description = "Смоки на bank и truck для выхода на A.",
                    landingSpot = "Закрывают bank и truck.",
                    imagePosition = "overpass_a_smokes_pos",
                    imageAim = "overpass_a_smokes_aim",
                    imageResult = "overpass_a_smokes_res"
                ),
                NadeEntity(
                    id = "overpass_monster_smoke",
                    mapId = "overpass",
                    title = "Monster One-Way Smoke",
                    side = "CT",
                    type = "SMOKE",
                    throwType = "LEFT_CLICK",
                    difficulty = 2,
                    position = "Стань в пленте B у бочки.",
                    aim = "Целься в угол стены у monster.",
                    description = "Дефолтный смок на monster, чтобы сдерживать выход T.",
                    landingSpot = "Проход monster на B.",
                    imagePosition = "overpass_monster_smoke_pos",
                    imageAim = "overpass_monster_smoke_aim",
                    imageResult = "overpass_monster_smoke_res"
                ),
                NadeEntity(
                    id = "overpass_toilets_flash",
                    mapId = "overpass",
                    title = "Toilets Flash",
                    side = "T",
                    type = "FLASH",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Встань у входа в туалеты со стороны фонтана.",
                    aim = "Кинь флешку над стеной внутрь туалетов.",
                    description = "Флешка для занятия toilets и выхода к A.",
                    landingSpot = "Над проходом в toilets.",
                    imagePosition = "overpass_toilets_flash_pos",
                    imageAim = "overpass_toilets_flash_aim",
                    imageResult = "overpass_toilets_flash_res"
                )
            )

            // ================= ANCIENT =================
            nades += listOf(
                NadeEntity(
                    id = "ancient_mid_smoke",
                    mapId = "ancient",
                    title = "Mid Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 2,
                    position = "Стань у стены на T‑спавне перед mid.",
                    aim = "Целься в вершину руин над mid.",
                    description = "Смок отрезает дефолтную позицию mid, облегчает выход к donut.",
                    landingSpot = "Центр mid.",
                    imagePosition = "ancient_mid_smoke_pos",
                    imageAim = "ancient_mid_smoke_aim",
                    imageResult = "ancient_mid_smoke_res"
                ),
                NadeEntity(
                    id = "ancient_donut_smoke",
                    mapId = "ancient",
                    title = "Donut Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "От mid подойди к стене у выхода к donut.",
                    aim = "Целься в угол арки donut.",
                    description = "Смок закрывает donut для выхода на A.",
                    landingSpot = "Вход в donut.",
                    imagePosition = "ancient_donut_smoke_pos",
                    imageAim = "ancient_donut_smoke_aim",
                    imageResult = "ancient_donut_smoke_res"
                ),
                NadeEntity(
                    id = "ancient_b_molotov",
                    mapId = "ancient",
                    title = "B Backsite Molotov",
                    side = "T",
                    type = "MOLOTOV",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Стань у входа на B через cave.",
                    aim = "Кинь молотов в глубокий угол плента B.",
                    description = "Молотов выжигает дальний угол B, где любят сидеть опорники.",
                    landingSpot = "Backsite B.",
                    imagePosition = "ancient_b_molo_pos",
                    imageAim = "ancient_b_molo_aim",
                    imageResult = "ancient_b_molo_res"
                )
            )

            // ================= TRAIN =================
            nades += listOf(
                NadeEntity(
                    id = "train_a_ivy_smoke",
                    mapId = "train",
                    title = "Ivy to A Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 2,
                    position = "Встань в ivy у правой стены.",
                    aim = "Целься в угол здания над зелёным поездом.",
                    description = "Смок закрывает cross к A из ivy.",
                    landingSpot = "Между зелёным и первым поездом.",
                    imagePosition = "train_ivy_smoke_pos",
                    imageAim = "train_ivy_smoke_aim",
                    imageResult = "train_ivy_smoke_res"
                ),
                NadeEntity(
                    id = "train_popdog_molotov",
                    mapId = "train",
                    title = "Popdog Molotov",
                    side = "CT",
                    type = "MOLOTOV",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Встань на A site у сотки.",
                    aim = "Кинь молотов в люк popdog.",
                    description = "Молотов задерживает быстрый спуск T через popdog.",
                    landingSpot = "Люк popdog.",
                    imagePosition = "train_popdog_molo_pos",
                    imageAim = "train_popdog_molo_aim",
                    imageResult = "train_popdog_molo_res"
                ),
                NadeEntity(
                    id = "train_b_upper_flash",
                    mapId = "train",
                    title = "B Upper Popflash",
                    side = "T",
                    type = "FLASH",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Стань на upper B перед выходом.",
                    aim = "Кинь флешку над перилом в плент.",
                    description = "Флешка для выхода на B с upper, ослепляет опорников в пленте.",
                    landingSpot = "Над B site.",
                    imagePosition = "train_b_upper_flash_pos",
                    imageAim = "train_b_upper_flash_aim",
                    imageResult = "train_b_upper_flash_res"
                )
            )

            // ================= ANUBIS =================
            nades += listOf(
                NadeEntity(
                    id = "anubis_mid_smoke",
                    mapId = "anubis",
                    title = "Mid to Connector Smoke",
                    side = "T",
                    type = "SMOKE",
                    throwType = "JUMP_THROW",
                    difficulty = 2,
                    position = "Встань в mid у фонтана.",
                    aim = "Целься в верх арки коннектора.",
                    description = "Смок отрезает коннектор, облегчая выход на A.",
                    landingSpot = "Вход в connector.",
                    imagePosition = "anubis_mid_smoke_pos",
                    imageAim = "anubis_mid_smoke_aim",
                    imageResult = "anubis_mid_smoke_res"
                ),
                NadeEntity(
                    id = "anubis_a_molotov",
                    mapId = "anubis",
                    title = "A Site Default Molotov",
                    side = "CT",
                    type = "MOLOTOV",
                    throwType = "LEFT_CLICK",
                    difficulty = 1,
                    position = "Встань на A ramp у ящика.",
                    aim = "Кинь молотов на дефолтный плент.",
                    description = "Молотов выжигает дефолт, не давая ставить бомбу.",
                    landingSpot = "Default A.",
                    imagePosition = "anubis_a_molo_pos",
                    imageAim = "anubis_a_molo_aim",
                    imageResult = "anubis_a_molo_res"
                ),
                NadeEntity(
                    id = "anubis_b_flash",
                    mapId = "anubis",
                    title = "B Site Popflash",
                    side = "T",
                    type = "FLASH",
                    throwType = "RIGHT_CLICK",
                    difficulty = 1,
                    position = "Стань у входа на B через руины.",
                    aim = "Кинь флешку над стеной в плент.",
                    description = "Pop‑флешка для выхода на B, ослепляет игроков в site и на stairs.",
                    landingSpot = "Над B site.",
                    imagePosition = "anubis_b_flash_pos",
                    imageAim = "anubis_b_flash_aim",
                    imageResult = "anubis_b_flash_res"
                )
            )

            // Вставка/обновление
            dao.insertAll(nades) // REPLACE обновит строки по первичному ключу (id). [web:176][web:162]
        }
    }
}
