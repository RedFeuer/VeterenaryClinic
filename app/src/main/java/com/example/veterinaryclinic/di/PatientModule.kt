package com.example.veterinaryclinic.di

import com.example.veterinaryclinic.data.repositoryImpl.OperationRepositoryImpl
import com.example.veterinaryclinic.domain.repositoryInterface.OperationRepository
import com.example.veterinaryclinic.domain.useCase.AddPatientUseCase
import com.example.veterinaryclinic.domain.useCase.ChangePatientUseCase
import com.example.veterinaryclinic.domain.useCase.DeletePatientUseCase
import com.example.veterinaryclinic.domain.useCase.ObservePatientsUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt-модуль доменного слоя для работы с пациентами.
 *
 * Назначение:
 * 1) Привязать контракт [OperationRepository] к его реализации [OperationRepositoryImpl]
 *    (чтобы доменный слой зависел от интерфейса, а не от конкретного класса).
 * 2) Предоставить use-case’ы для операций над пациентами: наблюдение, добавление, удаление, изменение.
 *
 * Все зависимости объявлены как singleton в рамках [SingletonComponent],
 * т.е. используются одним экземпляром на всё приложение.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PatientModule {
    /**
     * Привязывает интерфейс репозитория [OperationRepository] к реализации [OperationRepositoryImpl].
     * Это позволяет внедрять [OperationRepository] в use-case’ы и ViewModel,
     * не завязываясь на конкретный класс реализации.
     */
    @Binds
    @Singleton
    abstract fun bindOperationRepository(impl: OperationRepositoryImpl): OperationRepository

    companion object {
        /** Use case: получить поток списка пациентов (Flow) из репозитория. */
        @Provides
        @Singleton
        fun provideObservePatientUseCase(repository: OperationRepository): ObservePatientsUseCase{
            return ObservePatientsUseCase(repository)
        }

        /** Use case: добавить нового пациента в хранилище. */
        @Provides
        @Singleton
        fun provideAddPatientUseCase(repository: OperationRepository): AddPatientUseCase {
            return AddPatientUseCase(repository)
        }

        /** Use case: удалить пациента по идентификатору. */
        @Provides
        @Singleton
        fun provideDeletePatientUseCase(repository: OperationRepository): DeletePatientUseCase {
            return DeletePatientUseCase(repository)
        }

        /** Use case: обновить данные существующего пациента. */
        @Provides
        @Singleton
        fun provideChangePatientUseCase(repository: OperationRepository): ChangePatientUseCase {
            return ChangePatientUseCase(repository)
        }
    }
}