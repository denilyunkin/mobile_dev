package com.example.myapplication
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FurniDao {
    @Query("SELECT * FROM furnis")
    fun getAllFurnis(): Flow<List<Furni>>

    @Query("SELECT * FROM furnis WHERE id = :furniId")
    fun getFurniById(furniId: Int): Flow<Furni>

    @Insert
    suspend fun insertFurni(furni: Furni)

    @Query("DELETE FROM furnis")
    suspend fun clearAllFurnis()
}