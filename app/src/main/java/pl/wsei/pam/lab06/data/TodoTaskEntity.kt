package pl.wsei.pam.lab06.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.wsei.pam.lab06.TodoTask

@Entity(tableName = "todo_tasks")
data class TodoTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val deadline: String,
    val isDone: Boolean,
    val priority: String
) {
    fun toModel() = TodoTask(id, title, deadline, isDone, priority)

    companion object {
        fun fromModel(task: TodoTask) = TodoTaskEntity(
            id = task.id,
            title = task.title,
            deadline = task.deadline,
            isDone = task.isDone,
            priority = task.priority
        )
    }
}
