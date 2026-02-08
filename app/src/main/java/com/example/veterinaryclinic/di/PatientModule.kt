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

@Module
@InstallIn(SingletonComponent::class)
abstract class PatientModule {
    @Binds
    @Singleton
    abstract fun bindOperationRepository(impl: OperationRepositoryImpl): OperationRepository

    companion object {
        @Provides
        @Singleton
        fun provideObservePatientUseCase(repository: OperationRepository): ObservePatientsUseCase{
            return ObservePatientsUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideAddPatientUseCase(repository: OperationRepository): AddPatientUseCase {
            return AddPatientUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideDeletePatientUseCase(repository: OperationRepository): DeletePatientUseCase {
            return DeletePatientUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideChangePatientUseCase(repository: OperationRepository): ChangePatientUseCase {
            return ChangePatientUseCase(repository)
        }
    }
}