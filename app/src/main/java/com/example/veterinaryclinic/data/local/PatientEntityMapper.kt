package com.example.veterinaryclinic.data.local

import com.example.veterinaryclinic.domain.domainModel.Patient

/**
 * Маппер между слоями данных и домена.
 *
 * Преобразует:
 * - [PatientEntity] (модель хранения Room) -> [Patient] (доменная модель)
 * - [Patient] (доменная модель) -> [PatientEntity] (модель хранения Room)
 *
 * Нужен, чтобы слой домена не зависел от Room-аннотаций и структуры хранения.
 */
class PatientEntityMapper {
    /**
     * Конвертирует сущность БД [PatientEntity] в доменную модель [Patient].
     */
    fun toDomainModel(entity: PatientEntity): Patient = Patient(
        id = entity.id,
        name = entity.name,
        type = entity.type,
        customType = entity.customType,
        sex = entity.sex,
        ageYears = entity.ageYears,
        comment = entity.comment,

    )

    /**
     * Конвертирует доменную модель [Patient] в сущность БД [PatientEntity].
     *
     * Используется перед сохранением или обновлением в Room.
     */
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