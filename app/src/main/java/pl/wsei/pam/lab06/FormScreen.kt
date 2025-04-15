package pl.wsei.pam.lab06

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.wsei.pam.lab06.viewmodel.TodoViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormScreen(navController: NavController, viewModel: TodoViewModel) {
    var taskTitle by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    var isDone by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")

    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                title = "Form",
                showBackIcon = true,
                route = "list"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Task title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Status:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !isDone,
                    onClick = { isDone = false }
                )
                Text("W trakcie", modifier = Modifier.padding(end = 16.dp))

                RadioButton(
                    selected = isDone,
                    onClick = { isDone = true }
                )
                Text("Zakończone")
            }

            Text("Priority", style = MaterialTheme.typography.bodyMedium)
            Row {
                listOf("Low", "Medium", "High").forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = priority == option,
                            onClick = { priority = option }
                        )
                        Text(text = option)
                    }
                }
            }

            Text(if (date.isEmpty()) {"You need to choose date!"} else  {"Data: $date"})
            Button(
                onClick = { showDatePicker(context) { selectedDate -> date = selectedDate } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick date")
            }

            Button(
                onClick = {
                    try {
                        val selectedDate = LocalDate.parse(date, dateFormatter)
                        val today = LocalDate.now()

                        if (selectedDate.isAfter(today)) {
                            val task = TodoTask(
                                id = 0,
                                title = taskTitle,
                                deadline = date,
                                isDone = isDone,
                                priority = priority
                            )
                            viewModel.addTask(task)
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Data musi być w przyszłości!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Niepoprawna data!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz")
            }
        }
    }
}

fun showDatePicker(context: android.content.Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(formattedDate)
        },
        year, month, day
    )

    // Ustaw minimalną datę na jutro
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    datePickerDialog.show()
}
