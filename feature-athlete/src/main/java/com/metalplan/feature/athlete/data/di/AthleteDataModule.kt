package com.metalplan.feature.athlete.data.di

import android.content.Context
import androidx.room.Room
import com.metalplan.domain.common.TimeProvider
import com.metalplan.feature.athlete.data.local.AthleteDatabase
import com.metalplan.feature.athlete.data.local.dao.AthleteDao
import com.metalplan.feature.athlete.data.repository.AthleteRepositoryImpl
import com.metalplan.feature.athlete.domain.repository.AthleteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AthleteDatabaseModule {
    @Provides
    @Singleton
    fun provideAthleteDatabase(@ApplicationContext context: Context): AthleteDatabase {
        return Room.databaseBuilder(
            context,
            AthleteDatabase::class.java,
            "athlete_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideAthleteDao(database: AthleteDatabase): AthleteDao = database.athleteDao()

    @Provides
    @Singleton
    fun provideTimeProvider(): TimeProvider = object : TimeProvider {
        override fun nowMillis(): Long = System.currentTimeMillis()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AthleteRepositoryModule {
    @Binds
    abstract fun bindAthleteRepository(impl: AthleteRepositoryImpl): AthleteRepository
}
