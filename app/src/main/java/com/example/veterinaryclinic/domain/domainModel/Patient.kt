package com.example.veterinaryclinic.domain.domainModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Patient(
    val id: Long,
    val name: String,

    // тип выбирается из фиксированного списка типов животных
    val type: PatientType = PatientType.OTHER,

    /* если type == OTHER — сюда кладём введённый пользователем тип */
    val customType: String? = null,

    val sex: Sex = Sex.UNKNOWN,
    val ageYears: Int = 0,

    val comment: String? = null,
)

@Serializable
enum class Sex {
    @SerialName("Самец")
    MALE,
    @SerialName("Самка")
    FEMALE,
    @SerialName("Не указан")
    UNKNOWN
}

@Serializable
enum class PatientType {
    @SerialName("Кот")
    CAT,

    @SerialName("Собака")
    DOG,

    @SerialName("Птица")
    BIRD,

    @SerialName("Кролик")
    RABBIT,

    @SerialName("Другое")
    OTHER,
}