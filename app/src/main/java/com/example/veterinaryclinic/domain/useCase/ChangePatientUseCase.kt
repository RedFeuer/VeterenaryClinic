package com.example.veterinaryclinic.domain.useCase

import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository

/**
 * Use-case редактирования данных пациента.
 *
 * Инкапсулирует бизнес-операцию "редактировать пациента" и изолирует UI и ViewModel
 * от деталей работы с источником данных (Room, сеть, кэш).
 *
 * @property repository Репозиторий, который выполняет фактическое обновление пациента.
 */
class ChangePatientUseCase(
    private val repository: OperationRepository
) {
    suspend operator fun invoke(patient: Patient) {
        repository.changePatient(patient)
    }
}