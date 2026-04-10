package ru.aminovniaz.firstapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("tasks_prefs")

class TaskRepository(private val context: Context) {

    private val gson = Gson()
    private val TASKS_KEY = stringPreferencesKey("tasks_list")

    suspend fun saveTasks(tasks: List<Task>) {
        val json = gson.toJson(tasks)
        context.dataStore.edit { preferences ->
            preferences[TASKS_KEY] = json
        }
    }

    val tasksFlow: Flow<List<Task>> = context.dataStore.data
        .map { preferences ->
            val json = preferences[TASKS_KEY] ?: return@map emptyList()
            val type = object : TypeToken<List<Task>>() {}.type
            gson.fromJson(json, type) as List<Task>
        }

    suspend fun deleteTaskById(taskId: String, currentTasks: List<Task>) {
        val updatedTasks = currentTasks.filter { it.id != taskId }
        saveTasks(updatedTasks)
    }
}