package com.example.veterinaryclinic.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.veterinaryclinic.domain.domainModel.PatientType
import com.example.veterinaryclinic.domain.domainModel.Sex

@Entity(tableName = "app_patients")
data class PatientEntity (
    @PrimaryKey(autoGenerate = true) // по id определяем уникальность пациента в БД
    val id: Long = 0L, // по умолчанию 0 + происходит автогенерация id самим Room
    val name: String,

    // тип выбирается из фиксированного списка типов животных
    val type: PatientType = PatientType.OTHER,

    /* если type == OTHER — сюда кладём введённый пользователем тип */
    val customType: String? = null,

    val sex: Sex = Sex.UNKNOWN,
    val ageYears: Int = 0,

    val comment: String? = null,
)