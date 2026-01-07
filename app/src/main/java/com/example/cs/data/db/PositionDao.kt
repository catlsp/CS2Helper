package com.example.cs.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PositionDao {

    @Query("SELECT * FROM positions WHERE mapId = :mapId")
    suspend fun getPositionsForMap(mapId: String): List<PositionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(positions: List<PositionEntity>)
}
