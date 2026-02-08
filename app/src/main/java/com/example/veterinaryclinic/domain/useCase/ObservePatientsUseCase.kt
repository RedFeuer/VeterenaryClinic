package com.example.veterinaryclinic.domain.useCase

import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository

/**
 * Use-case отображения за списком пациентов.
 *
 * Возвращает поток данных (Flow), который эмитит актуальный список пациентов
 * при любых изменениях в источнике данных (Room).
 *
 * Используется в [AppViewModel] для реактивного обновления состояния UI.
 *
 * @property repository Репозиторий, предоставляющий поток пациентов.
 */
class ObservePatientsUseCase(
    private val repository: OperationRepository
) {
    operator fun invoke() = repository.observePatients()
}