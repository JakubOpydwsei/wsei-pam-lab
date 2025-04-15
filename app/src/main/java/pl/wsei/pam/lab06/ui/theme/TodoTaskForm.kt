package pl.wsei.pam.lab06.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.wsei.pam.lab06.TodoTask

@Composable
fun TodoTaskForm(onAddTask: (TodoTask) -> Unit) {
    var title by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Niski") }
    var isDone by remember { mutableStateOf(false) }

    val priorities = listOf("Niski", "Średni", "Wysoki")

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tytuł") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = deadline,
            onValueChange = { deadline = it },
            label = { Text("Deadline (RRRR-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Priorytet:")
        priorities.forEach { level ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(
                    selected = priority == level,
                    onClick = { priority = level }
                )
                Text(level)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(
                checked = isDone,
                onCheckedChange = { isDone = it }
            )
            Text("Czy wykonane?")
        }

        Button(
            onClick = {
                if (title.isNotBlank() && deadline.isNotBlank()) {
                    val task = TodoTask(
                        id = 0,
                        title = title,
                        deadline = deadline,
                        isDone = isDone,
                        priority = priority
                    )
                    onAddTask(task)
                    title = ""
                    deadline = ""
                    isDone = false
                    priority = "Niski"
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Dodaj zadanie")
        }
    }
}
