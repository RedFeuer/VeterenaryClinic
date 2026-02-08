package com.example.veterinaryclinic.domain.useCase

import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository

/**
 * Use-case добавления нового пациента.
 *
 * Инкапсулирует бизнес-операцию создания пациента и делегирует сохранение в [OperationRepository].
 * Возвращает идентификатор, который был присвоен пациенту при сохранении (настроена автогенерация в Room).
 *
 * @property repository Репозиторий, выполняющий операцию добавления пациента.
 */
class AddPatientUseCase(
    private val repository: OperationRepository
) {
    suspend operator fun invoke(patient: Patient) : Long {
        return repository.addPatient(patient)
    }
}