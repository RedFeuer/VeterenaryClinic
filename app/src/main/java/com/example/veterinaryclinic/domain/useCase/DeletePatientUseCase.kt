package com.example.veterinaryclinic.domain.useCase

import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository

class DeletePatientUseCase(
    private val repository: OperationRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.removePatient(id)
    }
}