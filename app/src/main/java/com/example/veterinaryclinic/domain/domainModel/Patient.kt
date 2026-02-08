package com.example.veterinaryclinic.domain.domainModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Доменная модель пациента ветеринарной клиники.
 *
 * Это «чистые» данные пациента, которые используются в приложении на уровне домена и UI:
 * - отображаются в списке пациентов,
 * - передаются в use-case’ы добавления, редактирования, удаления,
 * - могут маппиться в сущность Room (PatientEntity) для хранения в базе.
 *
 * @property id Уникальный идентификатор в базе данных (генерируется Room при вставке).
 * @property name Кличка пациента.
 * @property type Тип животного из фиксированного списка [PatientType].
 * @property customType Пользовательский тип, заполняется только когда [type] = [PatientType.OTHER].
 * @property sex Пол пациента из перечисления [Sex].
 * @property ageYears Возраст в годах (целое неотрицательное число).
 * @property comment Дополнительный комментарий (например: особенности, аллергии, заметки владельца).
 */
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

/**
 * Пол пациента.
 *
 * Аннотации @Serializable и @SerialName позволяют сериализовать/десериализовать значения
 * (например, для сетевого слоя или сохранения в JSON) в человекочитаемом виде.
 */
@Serializable
enum class Sex {
    @SerialName("Самец")
    MALE,
    @SerialName("Самка")
    FEMALE,
    @SerialName("Не указан")
    UNKNOWN
}

/**
 * Тип животного пациента.
 *
 * Значение [OTHER] используется, когда нужного типа нет в фиксированном списке —
 * в этом случае конкретный тип хранится в [Patient.customType].
 */
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