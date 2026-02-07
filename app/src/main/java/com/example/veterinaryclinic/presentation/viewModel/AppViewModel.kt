package com.example.veterinaryclinic.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.useCase.AddPatientUseCase
import com.example.veterinaryclinic.domain.useCase.ChangePatientUseCase
import com.example.veterinaryclinic.domain.useCase.DeletePatientUseCase
import com.example.veterinaryclinic.domain.useCase.ObservePatientsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val observePatientsUseCase: ObservePatientsUseCase,
    private val addPatientUseCase: AddPatientUseCase,
    private val deletePatientUseCase: DeletePatientUseCase,
    private val changePatientUseCase: ChangePatientUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<AppState.Content>(AppState.Content())
    val state: StateFlow<AppState.Content> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observePatientsUseCase()
                .onStart { _state.update { it.copy(isLoading = true, error = null) } }
                .catch { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
                .collect { patients ->
                    _state.update { it.copy(patients = patients, isLoading = false, error = null) }
                }
        }
    }

    fun onAddClick() {
        _state.update { it.copy(showAddDialog = true) }
    }

    fun onDismissAddDialog() {
        _state.update { it.copy(showAddDialog = false) }
    }

    fun onConfirmAddPatient(name: String, species: String) {
        val n = name.trim()
        val s = species.trim()
        if (n.isBlank() || s.isBlank()) return

        viewModelScope.launch {
            // id лучше генерировать в Room через autoGenerate, тогда здесь id не нужен.
            addPatientUseCase(Patient(id = 0L, name = n, species = s))
            _state.update { it.copy(showAddDialog = false) }
        }
    }

    fun onDeletePatient(patientId: Long) {
        viewModelScope.launch { deletePatientUseCase(patientId) }
    }
}