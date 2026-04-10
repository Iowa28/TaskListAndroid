package ru.aminovniaz.firstapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aminovniaz.firstapp.data.Task
import ru.aminovniaz.firstapp.data.TaskRepository

class MainViewModel(private val repository: TaskRepository) : ViewModel() {

    private val tasksMutable = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = tasksMutable.asStateFlow()

    init {
        viewModelScope.launch {
            repository.tasksFlow.collect { loadedTasks ->
                tasksMutable.value = loadedTasks
            }
        }
    }

    fun addTask(taskName: String) {
        viewModelScope.launch {
            val newTask = Task(name = taskName)
            val currentList = tasksMutable.value.toMutableList()
            currentList.add(newTask)
            tasksMutable.value = currentList
            repository.saveTasks(currentList)
        }
    }

    fun deleteTaskById(taskId: String) {
        viewModelScope.launch {
            val currentList = tasksMutable.value
            repository.deleteTaskById(taskId, currentList)
        }
    }
}