package com.example.veterinaryclinic.domain.useCase

import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository

class ChangePatientUseCase(
    private val repository: OperationRepository
) {
    suspend operator fun invoke(patient: Patient) {
        repository.changePatient(patient)
    }
}