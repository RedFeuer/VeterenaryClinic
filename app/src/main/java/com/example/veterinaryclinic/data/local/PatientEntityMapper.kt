package com.example.veterinaryclinic.data.local

import com.example.veterinaryclinic.domain.domainModel.Patient

class PatientEntityMapper {
    fun toDomainModel(entity: PatientEntity): Patient = Patient(
        id = entity.id,
        name = entity.name,
        type = entity.type,
        customType = entity.customType,
        sex = entity.sex,
        ageYears = entity.ageYears,
        comment = entity.comment,

    )

    fun toEntity(domainModel: Patient): PatientEntity = PatientEntity(
        id = domainModel.id,
        name = domainModel.name,
        type = domainModel.type,
        customType = domainModel.customType,
        sex = domainModel.sex,
        ageYears = domainModel.ageYears,
        comment = domainModel.comment,
    )
}