package com.example.veterinaryclinic.data.repositoryImpl

import com.example.veterinaryclinic.data.local.PatientDao
import com.example.veterinaryclinic.data.local.PatientEntityMapper
import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OperationRepositoryImpl(
    private val dao: PatientDao, // DI для работы с БД Room
    private val patientEntityMapper: PatientEntityMapper, // маппер entity <-> domainModel
): OperationRepository {
    /* вывод пациентов в виде списка */
    override fun observePatients(): Flow<List<Patient>> {
        return dao.getPatients().map{ entities ->
            entities.map { entity ->
                patientEntityMapper.toDomainModel(entity)
            }
            /* для пустого entities надо бы придумать что-нибудь */
        }
    }

    /* добавление нового пациента в список */
    override suspend fun addPatient(patient: Patient) {
        val patientEntity = patientEntityMapper.toEntity(patient)
        dao.addPatient(patientEntity)
    }

    override suspend fun removePatient(id: Long) {
        dao.deletePatient(id)
    }

    override suspend fun changePatient(newPatient: Patient) {
        val patientEntity = patientEntityMapper.toEntity(newPatient)
        dao.changePatient(patientEntity)
    }

}