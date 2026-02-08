package com.example.veterinaryclinic.presentation.viewModel

import com.example.veterinaryclinic.domain.domainModel.Patient

/**
 * Состояние главного экрана приложения.
 *
 * Используется UI-слоем (Compose) для отображения списка пациентов и управления диалогами
 * (добавление, редактирование, удаление), а также для обработки загрузки и ошибок.
 *
 */
sealed interface AppState {
    /**
     * Заготовка под отдельное состояние «фатальная ошибка».
     * В текущем MVP не используется напрямую, потому что ошибки передаются через поле [Content.error].
     */
    data object Error: AppState

    /**
     * Заготовка под отдельное состояние «глобальная загрузка».
     * В текущем MVP загрузка отображается через поле [Content.isLoading].
     */
    data object Loading: AppState

    /**
     * Основное рабочее состояние экрана.
     *
     * @property patients Текущий список пациентов, который отображается в списке.
     * @property showAddDialog Флаг показа диалога добавления пациента.
     * @property editingPatient Пациент, который сейчас редактируется (если не null — показываем диалог редактирования).
     * @property deletingPatient Пациент, выбранный для удаления (если не null — показываем диалог подтверждения удаления).
     * @property isLoading Признак фоновой загрузки данных (показываем индикатор).
     * @property error Текст ошибки для показа пользователю (например, через Snackbar). После показа может сбрасываться.
     */
    data class Content (
        val patients: List<Patient> = emptyList(),
        val showAddDialog: Boolean = false,
        val editingPatient: Patient? = null,
        val deletingPatient: Patient? = null,
        val isLoading: Boolean = true,
        val error: String? = null
    )
}