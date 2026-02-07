package com.example.veterinaryclinic.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface PatientDao {
    @Query("SELECT * FROM app_patients") // получить все(*) из таблицы в List
    fun getPatients(): Flow<List<PatientEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // не меняем, для этого будет специальая функция замены
    suspend fun addPatient(patient: PatientEntity)

    @Query("DELETE FROM app_patients WHERE id = :id") // удаление пациента по его id
    suspend fun deletePatient(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE) // заменяем старого пациента на нового
    suspend fun changePatient(patient: PatientEntity)
}