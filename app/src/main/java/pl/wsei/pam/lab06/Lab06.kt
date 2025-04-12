package pl.wsei.pam.lab06

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Lab06 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

class TaskViewModel : ViewModel() {
    private val _tasks = mutableStateListOf<TodoTask>(
        TodoTask("Programming", LocalDate.of(2024, 4, 18), false, Priority.Low, 1),
        TodoTask("Teaching", LocalDate.of(2024, 5, 12), false, Priority.High, 2),
        TodoTask("Learning", LocalDate.of(2024, 6, 28), true, Priority.Low, 3),
        TodoTask("Cooking", LocalDate.of(2024, 8, 18), false, Priority.Medium, 4)
    )

    val tasks: List<TodoTask> get() = _tasks

    fun addTask(task: TodoTask) {
        _tasks.add(task)
    }
}


@Composable
fun MyApp() {
    val navController = rememberNavController()
    val taskViewModel: TaskViewModel = viewModel()  // Obtain the ViewModel

    NavHost(navController = navController, startDestination = "list_screen") {
        composable("list_screen") { ListScreen(navController, taskViewModel) }
        composable("form_screen") { FormScreen(navController, taskViewModel) }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController, title: String, showBackIcon: Boolean, route: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = title) },
        navigationIcon = {
            if (showBackIcon) {
                IconButton(onClick = { navController.navigate(route) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (route !== "form") { // !@!@!@!!@!@!@!!@!@!@!@!@!@!!@!@!@!!@
                OutlinedButton(onClick = { navController.navigate("list") }) {
                    Text(text = "Zapisz", fontSize = 18.sp)
                }
            } else {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "")
                }
            }
        }
    )
}

data class TodoTask(
    val title: String,
    val deadline: LocalDate,
    val isDone: Boolean,
    val priority: Priority,
    val id: Int
)

enum class Priority {
    High, Medium, Low
}

fun todoTasks(): MutableList<TodoTask> {
    return mutableListOf(
        TodoTask("Programming", LocalDate.of(2024, 4, 18), false, Priority.Low, 1),
        TodoTask("Teaching", LocalDate.of(2024, 5, 12), false, Priority.High, 2),
        TodoTask("Learning", LocalDate.of(2024, 6, 28), true, Priority.Low, 3),
        TodoTask("Cooking", LocalDate.of(2024, 8, 18), false, Priority.Medium, 4),
    )
}

@Composable
fun ListScreen(navController: NavController, taskViewModel: TaskViewModel) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add task",
                        modifier = Modifier.scale(1.5f)
                    )
                },
                onClick = {
                    navController.navigate("form_screen")
                }
            )
        },
        topBar = {
            AppTopBar(
                title = "Lista zadań",
                showBackIcon = false,
                navController = navController,
                route = "list_screen"
            )
        },
        content = {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(taskViewModel.tasks) { task ->
                    ListItem(task = task)
                }
            }
        }
    )
}


@Composable
fun ListItem(task: TodoTask) {
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Termin: ${formatter.format(Date.from(task.deadline.atStartOfDay(ZoneId.systemDefault()).toInstant()))}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Priorytet: ${task.priority}")
            Text(text = if (task.isDone) "Status: Zakończone" else "Status: W trakcie")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormScreen(navController: NavController, taskViewModel: TaskViewModel) {
    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    // Manage the state for the form
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(Date()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var priority by remember { mutableStateOf(Priority.Low) }
    var isDone by remember { mutableStateOf(false) }
    var id by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Dodaj zadanie",
                showBackIcon = true,
                navController = navController,
                route = "list_screen"
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (title.isNotBlank()) {
                    // Add the new task to the list
                    taskViewModel.addTask(
                        TodoTask(
                            title = title,
                            deadline = LocalDate.parse(dateFormatter.format(dueDate)),
                            isDone = isDone,
                            priority = priority,
                            id = id
                        )
                    )
                    // Navigate back to the list screen
                    navController.popBackStack()
                }
            }) {
                Icon(Icons.Default.Check, contentDescription = "Zapisz")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tytuł zadania") },
                modifier = Modifier.fillMaxWidth()
            )

            // Description field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Opis zadania") },
                modifier = Modifier.fillMaxWidth()
            )

            // Date picker and selected date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Data: ${dateFormatter.format(dueDate)}")
                Button(onClick = { showDatePicker = true }) {
                    Text("Wybierz datę")
                }
            }

            // Show date picker dialog if needed
            if (showDatePicker) {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, day)
                        dueDate = calendar.time
                        showDatePicker = false
                    },
                    dueDate.year + 1900,
                    dueDate.month,
                    dueDate.date
                ).show()
            }

            // Priority dropdown
            Text("Priorytet:")
            Row {
                Priority.values().forEach { p ->
                    OutlinedButton(
                        onClick = { priority = p },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(p.name)
                    }
                }
            }
        }
    }
}

