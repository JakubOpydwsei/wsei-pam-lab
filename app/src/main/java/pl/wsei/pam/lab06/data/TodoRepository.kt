package pl.wsei.pam.lab06.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.TodoTask

class TodoRepository(private val taskDao: TodoTaskDao) {

    fun insert(task: TodoTask) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.insert(TodoTaskEntity.fromModel(task))
        }
    }

    fun update(task: TodoTask) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.update(TodoTaskEntity.fromModel(task))
        }
    }

    fun delete(task: TodoTask) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.delete(task.id)
        }
    }

    fun getAllTasks() = taskDao.getAll().map { list ->
        list.map { it.toModel() }
    }

    suspend fun getNextUndoneTask(): TodoTask? {
        return taskDao.getNextUndoneTask()?.toModel()
    }
}
