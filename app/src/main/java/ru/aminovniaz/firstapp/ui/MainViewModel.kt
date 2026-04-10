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

    private val editingTaskMutable = MutableStateFlow<Task?>(null)
    val editingTask: StateFlow<Task?> = editingTaskMutable.asStateFlow()

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

    fun startEditing(task: Task) {
        editingTaskMutable.value = task
    }

    fun cancelEditing() {
        editingTaskMutable.value = null
    }

    fun updateTask(taskId: String, newName: String) {
        viewModelScope.launch {
            val updatedList = tasksMutable.value.map { task ->
                if (task.id == taskId) {
                    task.copy(name = newName)
                } else {
                    task
                }
            }
            tasksMutable.value = updatedList
            repository.saveTasks(updatedList)
            editingTaskMutable.value = null
        }
    }
}