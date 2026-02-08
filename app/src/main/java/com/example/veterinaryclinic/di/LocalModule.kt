package com.example.veterinaryclinic.di

import android.app.Application
import androidx.room.Room
import com.example.veterinaryclinic.data.local.AppDatabase
import com.example.veterinaryclinic.data.local.PatientDao
import com.example.veterinaryclinic.data.local.PatientEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = AppDatabase::class.java,
            name = AppDatabase.DATABASE_NAME
        )
            .addMigrations(
//                MIGRATION_1_2, // добавил колонки type, customType, sex, ageYears, comment
            )
            .build()
    }

    @Provides
    @Singleton
    fun providePatientDao(database: AppDatabase): PatientDao {
        return database.patientDao()
    }

    @Provides
    @Singleton
    fun providePatientEntityMapper(): PatientEntityMapper {
        return PatientEntityMapper()
    }
}