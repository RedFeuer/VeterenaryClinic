package com.example.veterinaryclinic.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM app_patients") // получить все(*) из таблицы в List
    fun getPatients(): Flow<List<PatientEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // не меняем, для этого будет специальая функция замены
    suspend fun addPatient(patient: PatientEntity)

    @Query("DELETE FROM app_patients WHERE id = :id") // удаление пациента по его id
    suspend fun deletePatient(id: Long)

    @Update // заменяем старого пациента на нового по PrimaryKey = id
    suspend fun changePatient(patient: PatientEntity)
}