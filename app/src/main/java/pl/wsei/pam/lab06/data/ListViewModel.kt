import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.wsei.pam.lab06.TodoTask

data class ListUiState(val tasks: List<TodoTask> = emptyList())

class ListViewModel(val repository: TodoTaskRepository) : ViewModel() {
    val listUiState: StateFlow<ListUiState> = repository.getAllAsStream()
        .map { ListUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ListUiState())
}
