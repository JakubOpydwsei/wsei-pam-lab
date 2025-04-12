import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pl.wsei.pam.lab06.Priority
import pl.wsei.pam.lab06.TodoTask
import java.time.LocalDate

data class TodoTaskUiState(
    val todoTask: TodoTask = TodoTask(
        id = 0,  // Ustawienie domyślnej wartości ID
        title = "",
        isDone = false,
        priority = Priority.Low,
        deadline = LocalDate.now()
    )
)


class FormViewModel(private val repository: TodoTaskRepository) : ViewModel() {
    var todoTaskUiState by mutableStateOf(TodoTaskUiState())  // Użycie delegata state
        private set

    suspend fun save() {
        if (validate()) {
            repository.insertItem(todoTaskUiState.todoTask) // Zapisanie zadania
        }
    }

    private fun validate(): Boolean {
        return todoTaskUiState.todoTask.title.isNotBlank()  // Walidacja tytułu zadania
    }
}
