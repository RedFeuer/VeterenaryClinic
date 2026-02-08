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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.veterinaryclinic.domain.domainModel.Patient
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
        onConfirmAdd = { name, species ->
            viewModel.onConfirmAddPatient(name, species)
        },
        onDeletePatient = { patientId ->
            viewModel.onDeletePatient(patientId)
        },

        onEditClick = {patient ->
            viewModel.onEditClick(patient)
        },
        onDismissEditDialog = { viewModel.onDismissEditDialog() },
        onConfirmEdit = { patientId, name, species ->
            viewModel.onConfirmChangePatient(patientId, name, species)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppScreen(
    state: AppState.Content,
    onAddClick: () -> Unit,
    onDismissAddDialog: () -> Unit,
    onConfirmAdd: (name: String, species: String) -> Unit,
    onDeletePatient: (patientId: Long) -> Unit,
    onEditClick: (patient: Patient) -> Unit,
    onDismissEditDialog: () -> Unit,
    onConfirmEdit: (patientId: Long, name: String, species: String) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
                                onDelete = { onDeletePatient(patient.id) }
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
            initialSpecies = "",
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
            initialSpecies = editing.species,
            onDismiss = onDismissEditDialog,
            onConfirm = { name, species -> onConfirmEdit(editing.id, name, species) },
            confirmText = "Сохранить"
        )
    }
}

@Composable
private fun PatientRow(
    patient: Patient,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(text = patient.species, style = MaterialTheme.typography.bodyMedium)
            }

            TextButton(onClick = onEdit) { Text("Изменить") }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить пациента")
            }
        }
    }
}

@Composable
private fun PatientDialog(
    title: String,
    initialName: String,
    initialSpecies: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, species: String) -> Unit,
    confirmText: String,
) {
    var name by rememberSaveable(initialName) { mutableStateOf(initialName) }
    var species by rememberSaveable(initialSpecies) { mutableStateOf(initialSpecies) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    label = { Text("Кличка") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = species,
                    onValueChange = { species = it },
                    singleLine = true,
                    label = { Text("Вид (кот/собака)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, species) },
                enabled = name.isNotBlank() && species.isNotBlank()
            ) { Text(confirmText) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}