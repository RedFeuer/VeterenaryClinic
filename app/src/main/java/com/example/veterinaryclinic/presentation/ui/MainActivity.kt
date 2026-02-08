package com.example.veterinaryclinic.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.veterinaryclinic.ui.theme.VeterinaryClinicTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Подключает Hilt через @AndroidEntryPoint:
 *    - необходимо, если внутри Compose используется hiltViewModel()
 *      или если Activity/Composable-дерево завязано на DI через Hilt.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeterinaryClinicTheme {
                AppRoot()
            }
        }
    }
}