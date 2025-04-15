package pl.wsei.pam.lab06

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.wsei.pam.lab06.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@Composable
fun ListScreen(navController: NavController, viewModel: TodoViewModel) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = androidx.compose.foundation.shape.CircleShape,
                onClick = {
                    navController.navigate("form")
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add task",
                    modifier = Modifier.scale(1.5f)
                )
            }
        },
        topBar = {
            AppTopBar(
                navController = navController,
                title = "Lista zadań",
                showBackIcon = false,
                route = "form"
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(tasks) { task ->
                TaskItem(
                    item = task,
                    onCheckedChange = { checked ->
                        viewModel.updateTask(task.copy(isDone = checked))
                    },
                    onDelete = {
                        viewModel.deleteTask(task)
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(item: TodoTask, onCheckedChange: (Boolean) -> Unit, onDelete: () -> Unit) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Termin: ${item.deadline}")
            Spacer(modifier = Modifier.height(4.dp))
            Text("Priorytet: ${item.priority}")
            Text(text = if (item.isDone) "Status: Zakończone" else "Status: W trakcie")
        }
    }
}
