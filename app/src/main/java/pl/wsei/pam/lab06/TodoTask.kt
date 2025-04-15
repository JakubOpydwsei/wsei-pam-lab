package pl.wsei.pam.lab06

data class TodoTask(
    val id: Int = 0,
    val title: String,
    val deadline: String,
    val isDone: Boolean,
    val priority: String
)
