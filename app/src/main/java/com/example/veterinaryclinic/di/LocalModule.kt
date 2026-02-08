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

/**
 * Hilt-модуль локального слоя (Room).
 *
 * Отвечает за создание и предоставление singleton-зависимостей:
 * - базы данных [AppDatabase]
 * - DAO [PatientDao]
 * - маппера [PatientEntityMapper]
 *
 * Все зависимости живут в [SingletonComponent], т.е. один экземпляр на всё приложение.
 */
@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    /**
     * Создаёт singleton Room-базу приложения.
     * Здесь подключаются миграции при изменении схемы БД.
     */
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = AppDatabase::class.java,
            name = AppDatabase.DATABASE_NAME
        )
            .addMigrations(
                /* сюда добавлять миграции из Migrations.kt */
            )
            .build()
    }

    /** Предоставляет [PatientDao] для доступа к таблице пациентов. */
    @Provides
    @Singleton
    fun providePatientDao(database: AppDatabase): PatientDao {
        return database.patientDao()
    }

    /** Предоставляет маппер моделей Room <-> Domain. */
    @Provides
    @Singleton
    fun providePatientEntityMapper(): PatientEntityMapper {
        return PatientEntityMapper()
    }
}