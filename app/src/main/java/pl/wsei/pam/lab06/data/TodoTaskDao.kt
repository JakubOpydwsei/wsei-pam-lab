package pl.wsei.pam.lab06.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoTaskDao {

    @Insert
    suspend fun insert(task: TodoTaskEntity)

    @Query("SELECT * FROM todo_tasks")
    fun getAll(): Flow<List<TodoTaskEntity>>

    @Update
    suspend fun update(task: TodoTaskEntity)

    @Query("DELETE FROM todo_tasks WHERE id = :taskId")
    suspend fun delete(taskId: Int)

    @Query("SELECT * FROM todo_tasks WHERE isDone = 'false' ORDER BY deadline ASC LIMIT 1")
    suspend fun getNextUndoneTask(): TodoTaskEntity?
}
