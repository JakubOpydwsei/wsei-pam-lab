import android.content.Context

class AppDataContainer(private val context: Context) : AppContainer {
    override val todoTaskRepository: TodoTaskRepository by lazy {
        DatabaseTodoTaskRepository(AppDatabase.getInstance(context).taskDao())
    }
}
