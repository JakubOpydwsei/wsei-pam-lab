package pl.wsei.pam.lab06.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.wsei.pam.lab06.viewmodel.TodoViewModel

@Composable
fun TodoListScreen(viewModel: TodoViewModel) {
    val tasks by viewModel.tasks.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Lista zadań", style = MaterialTheme.typography.headlineSmall)

        LazyColumn {
            items(tasks) { task ->
                TodoTaskItem(
                    task = task,
                    onToggleDone = {
                        val updated = task.copy(isDone = !task.isDone)
                        viewModel.updateTask(updated)
                    },
                    onDelete = { viewModel.deleteTask(task) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        TodoTaskForm(onAddTask = { viewModel.addTask(it) })
    }
}
