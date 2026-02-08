package com.example.veterinaryclinic.domain.repositoryInterface

import com.example.veterinaryclinic.domain.domainModel.Patient
import kotlinx.coroutines.flow.Flow

interface OperationRepository {
    fun observePatients() : Flow<List<Patient>>

    suspend fun addPatient(patient: Patient) : Long

    suspend fun removePatient(id: Long)

    suspend fun changePatient(newPatient: Patient)
}