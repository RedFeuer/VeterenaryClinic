package com.example.veterinaryclinic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PatientEntity::class],
    version = 1,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun patientDao(): PatientDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}