package com.example.proyectofinal.data.local.dao

import androidx.room.*
import com.example.proyectofinal.data.local.entities.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM user_groups")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>): List<Long>

    @Query("DELETE FROM user_groups")
    suspend fun deleteAllGroups(): Int
}
