package com.example.veterinaryclinic.domain.useCase

import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository

/**
 * Use-case удаления пациента.
 *
 * Инкапсулирует бизнес-операцию "удалить пациента" и изолирует UI и ViewModel
 * от деталей работы с источником данных (Room, сеть, кэш).
 *
 * @property repository Репозиторий, который выполняет фактическое удаление пациента.
 */
class DeletePatientUseCase(
    private val repository: OperationRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.removePatient(id)
    }
}