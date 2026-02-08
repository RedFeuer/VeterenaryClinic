package com.example.veterinaryclinic.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.useCase.AddPatientUseCase
import com.example.veterinaryclinic.domain.useCase.ChangePatientUseCase
import com.example.veterinaryclinic.domain.useCase.DeletePatientUseCase
import com.example.veterinaryclinic.domain.useCase.ObservePatientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor (
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

    /* ДОБАВЛЕНИЕ ПАЦИЕНТА */
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

    /* РЕДАКТИРОВАНИЕ ПАЦИЕНТА */
    fun onEditClick(patient: Patient) {
        _state.update { it.copy(editingPatient = patient) }
    }

    fun onDismissEditDialog() {
        _state.update { it.copy(editingPatient = null) }
    }

    fun onConfirmChangePatient(patientId: Long, name: String, species: String) {
        val n = name.trim()
        val s = species.trim()
        if (n.isBlank() || s.isBlank()) return

        viewModelScope.launch {
            changePatientUseCase(Patient(id = patientId, name = n, species = s))
            _state.update { it.copy(editingPatient = null) }
        }
    }

    /* УДАЛЕНИЕ ПАЦИЕНТА */
    fun onDeletePatient(patientId: Long) {
        viewModelScope.launch { deletePatientUseCase(patientId) }
    }
}