package com.example.cs.ui.game2048

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.game2048DataStore by preferencesDataStore(name = "game_2048")

class Game2048Prefs(private val context: Context) {

    private val BEST_SCORE = intPreferencesKey("best_score")

    val bestScoreFlow: Flow<Int> = context.game2048DataStore.data.map { prefs ->
        prefs[BEST_SCORE] ?: 0
    }

    suspend fun setBestScore(value: Int) {
        context.game2048DataStore.edit { prefs ->
            prefs[BEST_SCORE] = value
        }
    }
}
