# VeterinaryClinic (Ветеринарная клиника)

Небольшое Android-приложение под тестовое задание **«Ветеринарная клиника»**.  
Цель — показать подход к разработке: архитектура, слои, DI, локальная БД, реактивные потоки, UI на Jetpack Compose.

---

## Что реализовано (MVP)

**Функция 1: Новый пациент**
- Просмотр списка пациентов (реактивно из БД).
- Добавление пациента.
- Редактирование пациента.
- Удаление пациента.

**Поля пациента**
- Имя (name)
- Тип животного (type) — выбирается из списка
- Другое (customType) — вводится, если `type == OTHER`
- Пол (sex)
- Возраст (ageYears)
- Комментарий (comment)

> Остальные функции (Запись к врачу, Медицинские карточки) отражены в схеме.
> Ссылка на отчет по выполнению: https://drive.google.com/file/d/1c72km2CFa-L929ncjUTX45AI9yX8E_GM/view?usp=sharing

---

## Технологии

- **Kotlin**
- **Jetpack Compose + Material 3**
- **MVVM** (ViewModel + StateFlow + state hoisting)
- **Clean Architecture**: `domain` / `data` / `presentation`
- **Room** (SQLite) + `Flow`
- **Hilt** (Dependency Injection)
- **Coroutines**

---

## Архитектура

### Слои

- **domain**
  - `Patient` (доменные модели)
  - интерфейсы репозиториев (контракты)
  - use-case’ы:
    - `ObservePatientsUseCase`
    - `AddPatientUseCase`
    - `ChangePatientUseCase`
    - `DeletePatientUseCase`

- **data**
  - `Room` база (`AppDatabase`)
  - `PatientEntity`, `PatientDao`
  - `TypeConverters` (enum -> String, String -> enum)
  - `Mapper` (`PatientEntity` ↔ `Patient`)

- **presentation**
  - `AppViewModel` (события UI → use-case’ы)
  - `AppState.Content` (единое состояние экрана) - в дальнейшем в sealed можно добавить другие состояния
  - Compose UI (экран со списком пациентов + диалоги добавления/редактирования)

---

## Состояние экрана

Экран работает по принципу:  
**UI только отображает `state` и отправляет события во ViewModel.**  
Все изменения данных выполняются в `ViewModel` через `useCase` → `repository` → `Room`.

Пример: `AppState.Content`
- `patients: List<Patient>`
- `showAddDialog: Boolean`
- `editingPatient: Patient?`
- `isLoading: Boolean`
- `error: String?`

---

## Схема базы данных (проектирование)

В рамках задания была подготовлена схема БД под функции:

1) Новый пациент  
2) Запись к врачу  
3) Медицинские карточки  

### Таблицы (концептуально)

- `app_patients` — пациенты
- `app_doctors` — врачи
- `app_appointments` — записи на прием
- `app_medical_cards` — медкарты (1:1 с пациентом)
- `app_medical_records` — записи в медкарте (история посещений)

### Связи (смысл)

- `Patient 1 — 1 MedicalCard`
- `MedicalCard 1 — N MedicalRecord`
- `Patient 1 — N Appointment`
- `Doctor 1 — N Appointment`
- `Doctor 1 — N MedicalRecord`
- `Appointment 0..1 — 0..1 MedicalRecord`

> В MVP фактически используется таблица пациентов (`app_patients`), остальное — как расширение.

---

## Как запустить

### Требования
- Android Studio - актуальная (2025.2.3)
- Android SDK 24+
- Gradle
- AGP 9+

### Запуск
1. Открыть проект в Android Studio
2. Дождаться Sync Gradle
3. Запустить конфигурацию `app`


