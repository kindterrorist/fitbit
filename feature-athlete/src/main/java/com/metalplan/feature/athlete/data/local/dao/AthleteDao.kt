package com.metalplan.feature.athlete.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.metalplan.feature.athlete.data.local.entity.AthleteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AthleteDao {
    @Query(
        """
        SELECT * FROM athletes
        WHERE (:query IS NULL OR fullName LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%')
          AND (:activeOnly = 0 OR active = 1)
        ORDER BY updatedAt DESC
        """
    )
    fun observeAthletes(query: String?, activeOnly: Boolean): Flow<List<AthleteEntity>>

    @Query("SELECT * FROM athletes WHERE id = :id LIMIT 1")
    suspend fun getAthleteById(id: String): AthleteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(athlete: AthleteEntity)

    @Query("DELETE FROM athletes WHERE id = :id")
    suspend fun deleteById(id: String)
}
