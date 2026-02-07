package com.example.veterinaryclinic.data.local

import com.example.veterinaryclinic.domain.domainModel.Patient

class PatientEntityMapper {
    fun toDomainModel(entity: PatientEntity): Patient = Patient(
        id = entity.id,
        name = entity.name,
        species = entity.species,
    )

    fun toEntity(domainModel: Patient): PatientEntity = PatientEntity(
        id = domainModel.id,
        name = domainModel.name,
        species = domainModel.species,
    )
}