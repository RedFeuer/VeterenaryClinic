package com.example.veterinaryclinic.data.repositoryImpl

import com.example.veterinaryclinic.data.local.PatientDao
import com.example.veterinaryclinic.data.local.PatientEntityMapper
import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Реализация [OperationRepository] на базе Room.
 *
 * Хранение выполняет [PatientDao], а преобразование между Entity и доменной моделью — [PatientEntityMapper].
 * Репозиторий является единой точкой доступа к данным для use-case'ов (связка domain -> data).
 */
class OperationRepositoryImpl @Inject constructor(
    private val dao: PatientDao, // DI для работы с БД Room
    private val patientEntityMapper: PatientEntityMapper, // маппер entity <-> domainModel
): OperationRepository {
    /**
     * Наблюдает за списком пациентов.
     *
     * @return Поток, который эмитит актуальный список [Patient] при изменениях в хранилище.
     */
    override fun observePatients(): Flow<List<Patient>> {
        return dao.getPatients().map{ entities ->
            entities.map { entity ->
                patientEntityMapper.toDomainModel(entity)
            }
            /* для пустого entities надо бы придумать что-нибудь */
        }
    }

    /**
     * Добавляет пациента в хранилище.
     *
     * @param patient Пациент в доменной модели.
     * @return ID, присвоенный пациенту при сохранении (автогенерация в Room).
     */
    override suspend fun addPatient(patient: Patient): Long {
        val patientEntity = patientEntityMapper.toEntity(patient)
        return dao.addPatient(patientEntity)
    }

    /**
     * Удаляет пациента по идентификатору.
     *
     * @param id ID пациента, которого нужно удалить.
     */
    override suspend fun removePatient(id: Long) {
        dao.deletePatient(id)
    }

    /**
     * Обновляет данные пациента.
     *
     * Обновление выполняется по Primary Key [Patient.id].
     *
     * @param newPatient Новая версия пациента (с тем же id).
     */
    override suspend fun changePatient(newPatient: Patient) {
        val patientEntity = patientEntityMapper.toEntity(newPatient)
        dao.changePatient(patientEntity)
    }

}