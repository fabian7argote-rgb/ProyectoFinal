package com.example.proyectofinal.data.local.dao

import androidx.room.*
import com.example.proyectofinal.data.local.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile LIMIT 1")
    fun getProfile(): Flow<ProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Long

    @Query("DELETE FROM profile")
    suspend fun deleteProfile(): Int
}
