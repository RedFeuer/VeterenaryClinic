package com.example.veterinaryclinic.domain.repositoryInterface

import com.example.veterinaryclinic.domain.domainModel.Patient
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий операций с пациентами.
 *
 * Это контракт слоя domain-data: UI и use-case'ы работают с [Patient] и не зависят от конкретной БД.
 * Реализация репозитория отвечает за:
 * - получение списка пациентов как потока данных;
 * - добавление, удаление и изменение пациента;
 * - преобразование моделей хранения (Entity) в доменную модель (и обратно).
 */
interface OperationRepository {
    /**
     * Наблюдает за списком пациентов.
     *
     * @return Поток, который эмитит актуальный список [Patient] при изменениях в хранилище.
     */
    fun observePatients() : Flow<List<Patient>>

    /**
     * Добавляет пациента в хранилище.
     *
     * @param patient Пациент в доменной модели.
     * @return ID, присвоенный пациенту при сохранении (автогенерация в Room).
     */
    suspend fun addPatient(patient: Patient) : Long

    /**
     * Удаляет пациента по идентификатору.
     *
     * @param id ID пациента, которого нужно удалить.
     */
    suspend fun removePatient(id: Long)

    /**
     * Обновляет данные пациента.
     *
     * Обновление выполняется по Primary Key [Patient.id].
     *
     * @param newPatient Новая версия пациента (с тем же id).
     */
    suspend fun changePatient(newPatient: Patient)
}