package com.example.cs.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class MapNadesCount(
    val mapId: String,
    val count: Int
)

@Dao
interface NadeDao {

    @Query("SELECT * FROM nades WHERE mapId = :mapId")
    suspend fun getNadesForMap(mapId: String): List<NadeEntity>

    @Query("SELECT * FROM nades WHERE mapId = :mapId")
    fun observeNadesForMap(mapId: String): Flow<List<NadeEntity>>

    // Один запрос вместо 8 отдельных запросов по каждой карте
    @Query("SELECT mapId, COUNT(*) AS count FROM nades GROUP BY mapId")
    suspend fun getCountsByMap(): List<MapNadesCount>

    @Query("SELECT COUNT(*) FROM nades")
    suspend fun getTotalNadesCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(nades: List<NadeEntity>)
}
