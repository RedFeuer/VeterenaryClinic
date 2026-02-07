package com.example.veterinaryclinic.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_patients")
data class PatientEntity (
    @PrimaryKey // по id определяем уникальность пациента в БД
    val id: Long,
    val name: String,
    val species: String,
)