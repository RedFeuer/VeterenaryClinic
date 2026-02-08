package com.example.veterinaryclinic.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.domainModel.PatientType
import com.example.veterinaryclinic.domain.domainModel.Sex
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

/**
 * Главная ViewModel экрана "Список пациентов" (MVP части "Новый пациент").
 *
 * Роли:
 * - хранит UI-состояние [AppState.Content] как StateFlow для Compose;
 * - подписывается на поток пациентов из [ObservePatientsUseCase] и обновляет список;
 * - обрабатывает пользовательские действия: добавить, изменить, удалить пациента;
 * - управляет показом диалогов (добавление, редактирование, подтверждение удаления)
 *   через поля состояния: showAddDialog, editingPatient, deletingPatient.
 *
 * DI:
 * - аннотация @HiltViewModel + @Inject constructor позволяет Hilt создать ViewModel и
 *   внедрить use-case'ы.
 */
@HiltViewModel
class AppViewModel @Inject constructor (
    private val observePatientsUseCase: ObservePatientsUseCase,
    private val addPatientUseCase: AddPatientUseCase,
    private val deletePatientUseCase: DeletePatientUseCase,
    private val changePatientUseCase: ChangePatientUseCase,
) : ViewModel() {

    /**
     * Внутреннее изменяемое состояние экрана.
     * Снаружи отдаём только read-only [StateFlow] через [state].
     */
    private val _state = MutableStateFlow<AppState.Content>(AppState.Content())

    /**
     * Публичное состояние экрана для подписки в Compose (collectAsStateWithLifecycle()).
     */
    val state: StateFlow<AppState.Content> = _state.asStateFlow()

    /**
     * При создании ViewModel запускаем подписку на пациентов.
     *
     * Логика:
     * - onStart: показываем loading, сбрасываем ошибку;
     * - catch: фиксируем ошибку в state (UI покажет snackbar);
     * - collect: обновляем список пациентов и выключаем loading.
     */
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

    /** Открывает диалог добавления пациента. */
    fun onAddClick() {
        _state.update { it.copy(showAddDialog = true) }
    }

    /** Закрывает диалог добавления пациента без сохранения. */
    fun onDismissAddDialog() {
        _state.update { it.copy(showAddDialog = false) }
    }

    /**
     * Подтверждение добавления пациента.
     *
     * Делает базовую валидацию:
     * - имя не пустое;
     * - возраст >= 0;
     * - если выбран тип OTHER — customType обязателен.
     *
     * После успешного добавления закрывает диалог.
     */
    fun onConfirmAddPatient(
        name: String,
        type: PatientType,
        customType: String,
        sex: Sex,
        ageYears: Int,
        comment: String,
    ) {
        val n = name.trim()
        val ct = customType.trim()
        val cmt = comment.trim()

        if (n.isBlank()) return
        if (ageYears < 0) return
        if (type == PatientType.OTHER && ct.isBlank()) return

        val customTypeOrNull = if (type == PatientType.OTHER) ct else null
        val commentOrNull = cmt.takeIf { it.isNotBlank() }

        viewModelScope.launch {
            addPatientUseCase(
                Patient(
                    id = 0L, // // Room autoGenerate в PatientEntity
                    name = n,
                    type = type,
                    customType = customTypeOrNull,
                    sex = sex,
                    ageYears = ageYears,
                    comment = commentOrNull
                )
            )
            _state.update { it.copy(showAddDialog = false) }
        }
    }

    /* РЕДАКТИРОВАНИЕ ПАЦИЕНТА */

    /** Открывает диалог редактирования, фиксируя редактируемого пациента в состоянии. */
    fun onEditClick(patient: Patient) {
        _state.update { it.copy(editingPatient = patient) }
    }

    /** Закрывает диалог редактирования без сохранения. */
    fun onDismissEditDialog() {
        _state.update { it.copy(editingPatient = null) }
    }

    /**
     * Подтверждение изменения пациента.
     *
     * Валидация аналогична добавлению.
     * После успешного изменения закрывает диалог редактирования.
     */
    fun onConfirmChangePatient(
        patientId: Long,
        name: String,
        type: PatientType,
        customType: String,
        sex: Sex,
        ageYears: Int,
        comment: String,
    ) {
        val n = name.trim()
        val ct = customType.trim()
        val cmt = comment.trim()

        if (n.isBlank()) return
        if (ageYears < 0) return
        if (type == PatientType.OTHER && ct.isBlank()) return

        val customTypeOrNull = if (type == PatientType.OTHER) ct else null
        val commentOrNull = cmt.takeIf { it.isNotBlank() }

        viewModelScope.launch {
            changePatientUseCase(
                Patient(
                    id = patientId,
                    name = n,
                    type = type,
                    customType = customTypeOrNull,
                    sex = sex,
                    ageYears = ageYears,
                    comment = commentOrNull
                )
            )
            _state.update { it.copy(editingPatient = null) }
        }
    }

    /* УДАЛЕНИЕ ПАЦИЕНТА */

    /**
     * Начинает сценарий удаления: сохраняет пациента в состоянии,
     * чтобы UI показал диалог подтверждения удаления.
     */
    fun onDeleteClick(patient: Patient) {
        _state.update { it.copy(deletingPatient = patient) }
    }

    /** Закрывает диалог подтверждения удаления без действия. */
    fun onDismissDeleteDialog() {
        _state.update { it.copy(deletingPatient = null) }
    }

    /**
     * Подтверждает удаление пациента, выбранного ранее в [onDeleteClick].
     * Если deletingPatient отсутствует — ничего не делает.
     */
    fun onConfirmDeletePatient() {
        val patientId = _state.value.deletingPatient?.id ?: return

        viewModelScope.launch {
            deletePatientUseCase(patientId)
            _state.update { it.copy(deletingPatient = null) }
        }
    }
}