package com.example.veterinaryclinic.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//val MIGRATION_1_2 = object:Migration(startVersion = 1, endVersion = 2) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        db.execSQL(
//            """
//            ALTER TABLE app_patients
//            ADD COLUMN type TEXT NOT NULL DEFAULT 'OTHER'
//            """.trimIndent()
//        )
//        db.execSQL(
//            """
//            ALTER TABLE app_patients
//            ADD COLUMN customType TEXT
//            """.trimIndent()
//        )
//        db.execSQL(
//            """
//            ALTER TABLE app_patients
//            ADD COLUMN sex TEXT NOT NULL DEFAULT 'UNKNOWN'
//            """.trimIndent()
//        )
//        db.execSQL(
//            """
//            ALTER TABLE app_patients
//            ADD COLUMN ageYears INTEGER NOT NULL DEFAULT 0
//            """.trimIndent()
//        )
//        db.execSQL(
//            """
//            ALTER TABLE app_patients
//            ADD COLUMN comment TEXT
//            """.trimIndent()
//        )
//    }
//}