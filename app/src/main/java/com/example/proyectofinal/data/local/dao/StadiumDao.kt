package com.example.proyectofinal.data.local.dao

import androidx.room.*
import com.example.proyectofinal.data.local.entities.StadiumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StadiumDao {
    @Query("SELECT * FROM stadiums")
    fun getAllStadiums(): Flow<List<StadiumEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStadiums(stadiums: List<StadiumEntity>): List<Long>

    @Query("DELETE FROM stadiums")
    suspend fun deleteAllStadiums(): Int
}
