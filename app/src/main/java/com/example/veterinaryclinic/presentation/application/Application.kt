package com.example.veterinaryclinic.presentation.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Главный класс приложения.
 *
 * Аннотация @HiltAndroidApp:
 * - включает Hilt во всём приложении;
 * - создаёт корневой DI-контейнер;
 * - позволяет использовать @AndroidEntryPoint и hiltViewModel().
 */
@HiltAndroidApp
class Application: Application()