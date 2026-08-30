package com.example.ui.theme

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppThemeManager {
    private const val PREFS_NAME = "trip_theme_prefs"
    private const val KEY_TEMPLATE_ID = "selected_template_id"
    private const val KEY_DARK_MODE = "dark_mode_preference" // "system", "dark", "light"

    private val _currentTemplate = MutableStateFlow(AppTemplate.OCEANIC_AZURE)
    val currentTemplate: StateFlow<AppTemplate> = _currentTemplate.asStateFlow()

    private val _darkModeOverride = MutableStateFlow<Boolean?>(null) // null = system, true = force dark, false = force light
    val darkModeOverride: StateFlow<Boolean?> = _darkModeOverride.asStateFlow()

    private var prefs: SharedPreferences? = null

    fun initialize(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val savedId = prefs?.getString(KEY_TEMPLATE_ID, AppTemplate.OCEANIC_AZURE.id) ?: AppTemplate.OCEANIC_AZURE.id
            val matched = AppTemplate.entries.find { it.id == savedId } ?: AppTemplate.OCEANIC_AZURE
            _currentTemplate.value = matched

            val darkPref = prefs?.getString(KEY_DARK_MODE, "system") ?: "system"
            _darkModeOverride.value = when (darkPref) {
                "dark" -> true
                "light" -> false
                else -> null
            }
        }
    }

    fun setTemplate(template: AppTemplate) {
        _currentTemplate.value = template
        prefs?.edit()?.putString(KEY_TEMPLATE_ID, template.id)?.apply()
    }

    fun setDarkModeOverride(isDark: Boolean?) {
        _darkModeOverride.value = isDark
        val prefVal = when (isDark) {
            true -> "dark"
            false -> "light"
            null -> "system"
        }
        prefs?.edit()?.putString(KEY_DARK_MODE, prefVal)?.apply()
    }
}
