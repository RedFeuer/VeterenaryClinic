package com.example.veterinaryclinic.domain.useCase

import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository

class AddPatientUseCase(
    private val repository: OperationRepository
) {
    suspend operator fun invoke(patient: Patient) : Long {
        return repository.addPatient(patient)
    }
}