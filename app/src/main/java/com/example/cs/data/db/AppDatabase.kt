package com.example.cs.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MapEntity::class,
        PositionEntity::class,
        NadeEntity::class,
        GuideCategoryEntity::class,
        GuideItemEntity::class
    ],
    version = 6
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun positionDao(): PositionDao
    abstract fun nadeDao(): NadeDao
    abstract fun guideDao(): GuideDao
}

