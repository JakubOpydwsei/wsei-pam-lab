package pl.wsei.pam.lab06.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.TodoTask
import pl.wsei.pam.lab06.data.TodoRepository
import pl.wsei.pam.lab06.notifications.AlarmScheduler

class TodoViewModel(
    private val repository: TodoRepository,
    private val app: Application
) : AndroidViewModel(app) {

    private val _tasks = MutableStateFlow<List<TodoTask>>(emptyList())
    val tasks: StateFlow<List<TodoTask>> = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllTasks().collect { tasks ->
                _tasks.value = tasks
                scheduleClosestAlarm(tasks)
            }
        }
    }

    fun addTask(task: TodoTask) {
        viewModelScope.launch {
            repository.insert(task)
        }
    }

    fun updateTask(task: TodoTask) {
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun deleteTask(task: TodoTask) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    private fun scheduleClosestAlarm(tasks: List<TodoTask>) {
        val next = tasks.filter { !it.isDone }
            .sortedBy { it.deadline }
            .firstOrNull()

        next?.let {
            AlarmScheduler.scheduleRepeatingAlarm(app.applicationContext, it)
        } ?: AlarmScheduler.cancelAlarm(app.applicationContext)
    }
}
