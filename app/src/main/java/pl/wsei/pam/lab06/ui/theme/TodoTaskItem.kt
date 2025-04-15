package pl.wsei.pam.lab06.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.wsei.pam.lab06.TodoTask

@Composable
fun TodoTaskItem(task: TodoTask, onToggleDone: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(task.title, style = MaterialTheme.typography.bodyLarge)
            Text("Termin: ${task.deadline}")
            Text("Priorytet: ${task.priority}")
        }
        Row {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onToggleDone() }
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Usuń")
            }
        }
    }
}
