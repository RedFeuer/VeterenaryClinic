package com.example.veterinaryclinic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Главная база данных приложения на Room.
 *
 * Содержит таблицы (entities), используемые приложением. В текущей версии хранит только пациентов
 * (таблица [PatientEntity]) — это реализованная часть функции «Новый пациент».
 *
 * Типы [PatientType] и [Sex] не являются примитивами, поэтому для их сохранения в БД используются
 * конвертеры [PatientTypeConverter] и [PatientSexConverter].
 *
 * @property version Версия схемы БД. При изменении структуры таблиц нужно увеличивать версию и
 * добавлять миграции.
 */
@Database(
    entities = [PatientEntity::class],
    version = 1,
)
@TypeConverters(PatientTypeConverter::class, PatientSexConverter::class)
abstract class AppDatabase: RoomDatabase() {
    /**
     * DAO для операций с пациентами.
     * Через него выполняются запросы к таблице пациентов (CRUD + Flow-наблюдение).
     */
    abstract fun patientDao(): PatientDao

    companion object {
        /** Имя файла базы данных (используется при создании RoomDatabase). */
        const val DATABASE_NAME = "app_database"
    }
}