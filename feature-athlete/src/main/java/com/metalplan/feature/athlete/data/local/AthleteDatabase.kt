package com.metalplan.feature.athlete.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.metalplan.feature.athlete.data.local.dao.AthleteDao
import com.metalplan.feature.athlete.data.local.entity.AthleteEntity

@Database(entities = [AthleteEntity::class], version = 1, exportSchema = true)
abstract class AthleteDatabase : RoomDatabase() {
    abstract fun athleteDao(): AthleteDao
}
