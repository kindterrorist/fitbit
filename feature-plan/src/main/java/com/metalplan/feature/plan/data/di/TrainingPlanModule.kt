package com.metalplan.feature.plan.data.di

import android.content.Context
import androidx.room.Room
import com.metalplan.feature.plan.data.local.TrainingPlanDatabase
import com.metalplan.feature.plan.data.local.dao.TrainingPlanDao
import com.metalplan.feature.plan.data.repository.TrainingPlanRepositoryImpl
import com.metalplan.feature.plan.domain.repository.TrainingPlanRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrainingPlanDatabaseModule {
    @Provides
    @Singleton
    fun provideTrainingPlanDatabase(@ApplicationContext context: Context): TrainingPlanDatabase {
        return Room.databaseBuilder(
            context,
            TrainingPlanDatabase::class.java,
            "training_plan_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTrainingPlanDao(database: TrainingPlanDatabase): TrainingPlanDao = database.trainingPlanDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class TrainingPlanRepositoryModule {
    @Binds
    abstract fun bindTrainingPlanRepository(impl: TrainingPlanRepositoryImpl): TrainingPlanRepository
}
