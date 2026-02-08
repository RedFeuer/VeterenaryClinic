package com.example.veterinaryclinic.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.veterinaryclinic.domain.domainModel.Patient
import com.example.veterinaryclinic.domain.domainModel.PatientType
import com.example.veterinaryclinic.domain.domainModel.Sex
import com.example.veterinaryclinic.presentation.viewModel.AppState
import com.example.veterinaryclinic.presentation.viewModel.AppViewModel


@Composable
internal fun AppRoot() {
    val viewModel = hiltViewModel<AppViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AppScreen(
        state = state,
        onAddClick = { viewModel.onAddClick() },
        onDismissAddDialog = { viewModel.onDismissAddDialog() },
        onConfirmAdd = { name, type, customType, sex, ageYears, comment ->
            viewModel.onConfirmAddPatient(
                name = name,
                type = type,
                customType = customType,
                sex = sex,
                ageYears = ageYears,
                comment = comment,
            )
        },
        onDeletePatient = { patient ->
            viewModel.onDeleteClick(patient)
        },
        onDismissDeleteDialog = { viewModel.onDismissDeleteDialog() },
        onConfirmDelete = { viewModel.onConfirmDeletePatient() },

        onEditClick = {patient ->
            viewModel.onDeleteClick(patient)
        },
        onDismissEditDialog = { viewModel.onDismissEditDialog() },
        onConfirmEdit = { patientId, name, type, customType, sex, ageYears, comment ->
            viewModel.onConfirmChangePatient(
                patientId = patientId,
                name = name,
                type = type,
                customType = customType,
                sex = sex,
                ageYears = ageYears,
                comment = comment,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppScreen(
    state: AppState.Content,
    onAddClick: () -> Unit,
    onDismissAddDialog: () -> Unit,
    onConfirmAdd: (
        name: String,
        type: PatientType,
        customType: String,
        sex: Sex,
        ageYears: Int,
        comment: String
    ) -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onConfirmDelete: () -> Unit,
    onDeletePatient: (patient: Patient) -> Unit,
    onEditClick: (patient: Patient) -> Unit,
    onDismissEditDialog: () -> Unit,
    onConfirmEdit: (
        patientId: Long,
        name: String,
        type: PatientType,
        customType: String,
        sex: Sex,
        ageYears: Int,
        comment: String
    ) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    /* вывод ошибок в снеках */
    LaunchedEffect(state.error) {
        val msg = state.error ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message = msg)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ветеринарная клиника") },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                /* Кнопка добавить пациентов */
                Button(onClick = onAddClick) { Text("Добавить пациента") }
            }

            /* Список пациентов */
            Box(modifier = Modifier.fillMaxSize()) {
                if (!state.isLoading && state.patients.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Пациентов пока нет")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = state.patients, key = { it.id }) { patient ->
                            PatientRow(
                                patient = patient,
                                onEdit = { onEditClick(patient) },
                                onDelete = { onDeletePatient(patient) }
                            )
                        }
                    }
                }

                /* состояние загрузки */
                if (state.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    /* Диалог добавления нового пациента */
    if (state.showAddDialog) {
        PatientDialog(
            title = "Новый пациент",
            initialName = "",
            initialType = PatientType.CAT,
            initialCustomType = "",
            initialSex = Sex.UNKNOWN,
            initialAgeYears = 0,
            initialComment = "",
            onDismiss = onDismissAddDialog,
            onConfirm = onConfirmAdd,
            confirmText = "Сохранить"
        )
    }

    /* Диалог обновления пациента */
    val editing = state.editingPatient
    if (editing != null) {
        PatientDialog(
            title = "Редактирование пациента",
            initialName = editing.name,
            initialType = editing.type,
            initialCustomType = editing.customType.orEmpty(),
            initialSex = editing.sex,
            initialAgeYears = editing.ageYears,
            initialComment = editing.comment.orEmpty(),
            onDismiss = onDismissEditDialog,
            onConfirm = { name, type, customType, sex, ageYears, comment ->
                onConfirmEdit(editing.id, name, type, customType, sex, ageYears, comment)
            },
            confirmText = "Сохранить"
        )
    }

    /* Диалог удаления пациента */
    val deleting = state.deletingPatient
    if (deleting != null) {
        AlertDialog(
            onDismissRequest = onDismissDeleteDialog,
            title = { Text("Удалить пациента?") },
            text = { Text("Вы уверены, что хотите удалить пациента «${deleting.name}»?") },
            confirmButton = {
                TextButton(onClick = onConfirmDelete) { Text("Да") }
            },
            dismissButton = {
                TextButton(onClick = onDismissDeleteDialog) { Text("Нет") }
            }
        )
    }
}

@Composable
private fun PatientRow(
    patient: Patient,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val typeText = when (patient.type) {
        PatientType.OTHER -> patient.customType?.takeIf { it.isNotBlank() } ?: "Другое"
        else -> patient.type.uiText()
    }

    val genderText = patient.sex.uiText()
    val ageText = "${patient.ageYears} лет"

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {

                /* Имя */
                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp))

                /* Тип • Пол • Возраст */
                Text(
                    text = "$typeText • $genderText • $ageText",
                    style = MaterialTheme.typography.bodyMedium
                )

                /* Комментарий (если есть) */
                patient.comment
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }
                    ?.let { comment ->
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = comment,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
            }

            /* Действия */
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onEdit) {
                    Text("Изменить")
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить пациента"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PatientDialog(
    title: String,
    initialName: String,
    initialType: PatientType,
    initialCustomType: String,
    initialSex: Sex,
    initialAgeYears: Int,
    initialComment: String,
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        type: PatientType,
        customType: String,
        gender: Sex,
        ageYears: Int,
        comment: String
    ) -> Unit,
    confirmText: String,
) {
    var name by rememberSaveable(initialName) { mutableStateOf(initialName) }

    var type by rememberSaveable(initialType.name) { mutableStateOf(initialType) }
    var customType by rememberSaveable(initialCustomType) { mutableStateOf(initialCustomType) }

    var gender by rememberSaveable(initialSex.name) { mutableStateOf(initialSex) }

    var ageText by rememberSaveable(initialAgeYears) { mutableStateOf(initialAgeYears.toString()) }
    var comment by rememberSaveable(initialComment) { mutableStateOf(initialComment) }

    var typeExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }

    val ageInt = ageText.toIntOrNull()
    val isValid =
        name.isNotBlank() &&
                (ageInt != null && ageInt >= 0) &&
                (type != PatientType.OTHER || customType.isNotBlank())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = if (type == PatientType.OTHER) "Другое" else type.uiText(),
                        onValueChange = {},
                        label = { Text("Тип") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        PatientType.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(if (option == PatientType.OTHER) "Другое" else option.uiText()) },
                                onClick = {
                                    type = option
                                    typeExpanded = false
                                    if (option != PatientType.OTHER) customType = ""
                                }
                            )
                        }
                    }
                }

                if (type == PatientType.OTHER) {
                    OutlinedTextField(
                        value = customType,
                        onValueChange = { customType = it },
                        singleLine = true,
                        label = { Text("Тип (другое)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = gender.uiText(),
                        onValueChange = {},
                        label = { Text("Пол") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        Sex.entries.forEach { g ->
                            DropdownMenuItem(
                                text = { Text(g.uiText()) },
                                onClick = {
                                    gender = g
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = ageText,
                    onValueChange = { ageText = it.filter(Char::isDigit).take(3) },
                    singleLine = true,
                    label = { Text("Возраст (лет)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Комментарий") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val age = ageText.toIntOrNull() ?: 0
                    onConfirm(name, type, customType, gender, age, comment)
                },
                enabled = isValid
            ) { Text(confirmText) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

private fun PatientType.uiText(): String = when (this) {
    PatientType.CAT -> "Кот"
    PatientType.DOG -> "Собака"
    PatientType.BIRD -> "Птица"
    PatientType.RABBIT -> "Кролик"
    PatientType.OTHER -> "Другое"
}

private fun Sex.uiText(): String = when (this) {
    Sex.MALE -> "Самец"
    Sex.FEMALE -> "Самка"
    Sex.UNKNOWN -> "Не указан"
}