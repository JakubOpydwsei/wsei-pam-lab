package pl.wsei.pam.lab02

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import pl.wsei.pam.lab02.R
import pl.wsei.pam.R


class Lab02Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab02)

        // Przypisz funkcje do przycisków
        val button6x6: Button = findViewById(R.id.main_6_6_board)
        val button4x4: Button = findViewById(R.id.main_4_4_board)
        val button4x3: Button = findViewById(R.id.main_4_3_board)
        val button3x2: Button = findViewById(R.id.main_3_2_board)

        val buttons = listOf(button6x6, button4x4, button4x3, button3x2)

        buttons.forEach { button ->
            button.setOnClickListener { v ->
                handleBoardSize(v)
            }
        }
    }

    private fun handleBoardSize(v: View) {
        // Pobieranie tagu przycisku
        val tag: String? = v.tag as String?
        val tokens: List<String>? = tag?.split(" ")
        val rows = tokens?.get(0)?.toInt()
        val columns = tokens?.get(1)?.toInt()

        // Wyświetlanie komunikatu z rozmiarem planszy
        Toast.makeText(this, "rows: $rows, columns: $columns", Toast.LENGTH_SHORT).show()
    }
}
