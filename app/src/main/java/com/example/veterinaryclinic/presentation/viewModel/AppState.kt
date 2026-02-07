package com.example.veterinaryclinic.presentation.viewModel

import com.example.veterinaryclinic.domain.domainModel.Patient

sealed interface AppState {
    /* можно добавить в качестве улучшиний. в MVP не нужно */
    data object Error: AppState

    data object Loading: AppState

    data class Content (
        val patients: List<Patient> = emptyList(),
        val showAddDialog: Boolean = false,
        val isLoading: Boolean = true,
        val error: String? = null
    )
}