package pl.wsei.pam.lab03

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.R
import java.util.*

class Lab03Activity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val allIcons = listOf(
        R.drawable.baseline_rocket_launch_24,
        R.drawable.sharp_accessibility_24,
        R.drawable.s1,
        R.drawable.s2,
        R.drawable.s3,
        R.drawable.s4,
        R.drawable.s5,
        R.drawable.s6,
        R.drawable.s7,
        R.drawable.s8,
        R.drawable.s9,
        R.drawable.s10,
        R.drawable.s11,
        R.drawable.s12,
        R.drawable.s13,
        R.drawable.s14,
        R.drawable.s15,
        R.drawable.s16,

    )
    private var matchedPairs = 0
    private val flippedTiles = mutableListOf<Tile>()
    private lateinit var logic: MemoryGameLogic
    private var isProcessing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab03)

        gridLayout = findViewById(R.id.gridLayout)
        val size = intent.getIntArrayExtra("size") ?: intArrayOf(4, 4)
        val rows = size[0]
        val columns = size[1]

        setupBoard(columns, rows)
    }

    private fun setupBoard(columns: Int, rows: Int) {
//        Log.d("MemoryGame", "Columns: $columns, Rows: $rows")
        val numPairs: Int

        when {
            columns == 2 && rows == 3 -> numPairs = 3
            columns == 3 && rows == 4 -> numPairs = 6
            columns == 4 && rows == 4 -> numPairs = 8
            columns == 6 && rows == 6 -> numPairs = 18
            else -> throw IllegalArgumentException("Unsupported board size")
        }
        Log.d("MemoryGame", "Pairs: $numPairs")
        val iconsToUse = allIcons.take(numPairs) + allIcons.take(numPairs)
        val shuffledIcons = iconsToUse.shuffled().toMutableList()

        gridLayout.columnCount = columns

        logic = MemoryGameLogic(numPairs)
        matchedPairs = 0

        gridLayout.removeAllViews()

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val button = ImageButton(gridLayout.context).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        // Ustawiamy na 0, aby pozwolić GridLayout rozciągać przyciski
                        width = 0
                        height = 0
                        // Określamy wagę, by przyciski rozciągały się proporcjonalnie do dostępnej przestrzeni
                        rowSpec = GridLayout.spec(i, 1f) // Waga w wierszu
                        columnSpec = GridLayout.spec(j, 1f) // Waga w kolumnie
                        setMargins(10, 10, 10, 10) // Marginesy dla przycisków
                    }
                    tag = "$i,$j" // Przypisanie tagu dla identyfikacji
                }

                gridLayout.addView(button)  // Dodanie przycisku do gridLayout


                val tile = Tile(button, shuffledIcons.removeAt(0), R.drawable.deck)
                tiles[button.tag.toString()] = tile

                button.setOnClickListener {
                    onTileClicked(tile)
                }
            }
        }
    }

    private fun onTileClicked(tile: Tile) {
        if (tile.revealed || flippedTiles.size == 2 || isProcessing) return

        tile.revealed = true
        flippedTiles.add(tile)

        if (flippedTiles.size == 2) {
            isProcessing = true

            val isMatch = flippedTiles[0].tileResource == flippedTiles[1].tileResource

            if (isMatch) {
                matchedPairs++
                flippedTiles.clear()
                isProcessing = false
                checkGameFinished()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    runOnUiThread {
                        flippedTiles.forEach { it.revealed = false }
                        flippedTiles.clear()
                        isProcessing = false
                    }
                }, 1000)
            }
        }
    }

    private fun checkGameFinished() {
        if (matchedPairs == logic.maxMatches) {
            Toast.makeText(this, "Game finished!", Toast.LENGTH_SHORT).show()
        }
    }

    private data class Tile(val button: ImageButton, val tileResource: Int, val deckResource: Int) {
        init {
            button.setImageResource(deckResource)
        }

        var revealed: Boolean = false
            set(value) {
                field = value
                button.setImageResource(if (value) tileResource else deckResource)
            }
    }

    private class MemoryGameLogic(val maxMatches: Int)

    private enum class GameStates { Matching, Match, NoMatch, Finished }
}

