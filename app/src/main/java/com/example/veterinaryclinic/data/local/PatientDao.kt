package com.example.veterinaryclinic.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы с таблицей пациентов `app_patients`.
 *
 * Слой Room, который предоставляет CRUD-операции для [PatientEntity]:
 * - чтение списка пациентов (реактивно через [Flow]);
 * - добавление нового пациента;
 * - удаление пациента по id;
 * - обновление данных пациента по первичному ключу.
 */
@Dao
interface PatientDao {
    /**
     * Возвращает поток списка пациентов из БД.
     *
     * [Flow] будет эмитить новое значение каждый раз, когда таблица `app_patients` изменяется
     * (добавление, удаление, обновление).
     */
    @Query("SELECT * FROM app_patients") // получить все(*) из таблицы в List
    fun getPatients(): Flow<List<PatientEntity>>

    /**
     * Добавляет нового пациента в таблицу.
     *
     * Стратегия конфликта: [OnConflictStrategy.ABORT] — при конфликте (по PrimaryKey)
     * операция будет прервана и Room выбросит исключение.
     *
     * @return id новой записи (автосгенерированный primary key).
     */
    @Insert(onConflict = OnConflictStrategy.ABORT) // не меняем, для этого будет специальая функция замены
    suspend fun addPatient(patient: PatientEntity) : Long // возвращаем ключ, автосгенеренный Room

    /**
     * Удаляет пациента по его идентификатору.
     *
     * Если записи с таким id нет — удаление не выполнит действий и ошибок не будет.
     */
    @Query("DELETE FROM app_patients WHERE id = :id") // удаление пациента по его id
    suspend fun deletePatient(id: Long)

    /**
     * Обновляет существующую запись пациента.
     *
     * Обновление происходит по PrimaryKey (поле `id` в [PatientEntity]).
     * Если записи с таким id нет — обновление не выполнит действий.
     */
    @Update // заменяем старого пациента на нового по PrimaryKey = id
    suspend fun changePatient(patient: PatientEntity)
}