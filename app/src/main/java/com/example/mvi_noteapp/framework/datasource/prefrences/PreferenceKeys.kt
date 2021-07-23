package com.example.mvi_noteapp.framework.datasource.prefrences

class PreferenceKeys {

    companion object{

        // Shared Preference Files:
        const val NOTE_PREFERENCES: String = "com.example.mvi_noteapp.notes"

        // Shared Preference Keys
        val NOTE_FILTER: String = "$NOTE_PREFERENCES.NOTE_FILTER"
        val NOTE_ORDER: String = "$NOTE_PREFERENCES.NOTE_ORDER"

    }
}