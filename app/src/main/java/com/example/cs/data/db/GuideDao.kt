package com.example.cs.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GuideDao {

    @Query("SELECT * FROM guide_categories")
    suspend fun getCategories(): List<GuideCategoryEntity>

    @Query("SELECT * FROM guide_items WHERE categoryId = :categoryId")
    suspend fun getItemsForCategory(categoryId: String): List<GuideItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<GuideCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<GuideItemEntity>)
}
