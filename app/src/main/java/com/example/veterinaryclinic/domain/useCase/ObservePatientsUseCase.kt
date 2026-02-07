package com.example.veterinaryclinic.domain.useCase

import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository

class ObservePatientsUseCase(
    private val repository: OperationRepository
) {
    operator fun invoke() = repository.observePatients()
}