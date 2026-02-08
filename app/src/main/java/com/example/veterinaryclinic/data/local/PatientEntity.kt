package com.example.veterinaryclinic.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_patients")
data class PatientEntity (
    @PrimaryKey(autoGenerate = true) // по id определяем уникальность пациента в БД
    val id: Long = 0L, // по умолчанию 0 + происходит автогенерация id самим Room
    val name: String,
    val species: String,
)