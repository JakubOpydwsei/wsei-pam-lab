package pl.wsei.pam.lab03

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
            else -> throw IllegalArgumentException("Otrzymano błędne wymiary!: -> Columns: $columns, Rows: $rows ?")
        }
//        Log.d("MemoryGame", "Pairs: $numPairs")
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
                        // Ustawiamy na 0, inaczej się nie rozciągnie
                        width = 0
                        height = 0
                        // waga aby zajmowały max przestrzeń
                        rowSpec = GridLayout.spec(i, 1f)
                        columnSpec = GridLayout.spec(j, 1f)
                        setMargins(10, 10, 10, 10)
                    }
                    tag = "$i,$j"
                }

                gridLayout.addView(button)


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

            val scaleUpX = ObjectAnimator.ofFloat(flippedTiles[0].button, "scaleX", 1f, 1.2f)
            val scaleUpY = ObjectAnimator.ofFloat(flippedTiles[0].button, "scaleY", 1f, 1.2f)
            val rotation = ObjectAnimator.ofFloat(flippedTiles[0].button, "rotation", 0f, 360f)
            val scaleDownX = ObjectAnimator.ofFloat(flippedTiles[0].button, "scaleX", 1.2f, 1f)
            val scaleDownY = ObjectAnimator.ofFloat(flippedTiles[0].button, "scaleY", 1.2f, 1f)
            val shakeX = ObjectAnimator.ofFloat(flippedTiles[0].button, "translationX", 0f, 10f, 0f)
            val shakeY = ObjectAnimator.ofFloat(flippedTiles[0].button, "translationY", 0f, 10f, 0f)
            val alpha = ObjectAnimator.ofFloat(flippedTiles[0].button, "alpha", 1f, 0.3f)

            val scaleUpX2 = ObjectAnimator.ofFloat(flippedTiles[1].button, "scaleX", 1f, 1.2f)
            val scaleUpY2 = ObjectAnimator.ofFloat(flippedTiles[1].button, "scaleY", 1f, 1.2f)
            val rotation2 = ObjectAnimator.ofFloat(flippedTiles[1].button, "rotation", 0f, 360f)
            val scaleDownX2 = ObjectAnimator.ofFloat(flippedTiles[1].button, "scaleX", 1.2f, 1f)
            val scaleDownY2 = ObjectAnimator.ofFloat(flippedTiles[1].button, "scaleY", 1.2f, 1f)
            val shakeX2 = ObjectAnimator.ofFloat(flippedTiles[1].button, "translationX", 0f, 10f, 0f)
            val shakeY2 = ObjectAnimator.ofFloat(flippedTiles[1].button, "translationY", 0f, 10f, 0f)
            val alpha2 = ObjectAnimator.ofFloat(flippedTiles[1].button, "alpha", 1f, 0.3f)

            val animatorMatch = AnimatorSet()
            animatorMatch.playSequentially(
                AnimatorSet().apply { playTogether(scaleUpX, scaleUpY,scaleUpX2, scaleUpY2) },
                AnimatorSet().apply { playTogether(rotation,rotation2) },

                AnimatorSet().apply { playTogether(scaleDownX, scaleDownY,scaleDownX2, scaleDownY2) },
                AnimatorSet().apply { playTogether(alpha,alpha2) },

            )
            animatorMatch.duration = 500L

            val animatorFalse = AnimatorSet()
            animatorFalse.playSequentially(
                AnimatorSet().apply { playTogether(shakeX, shakeY,shakeX2, shakeY2) },
            )
            animatorFalse.duration = 300L


            if (isMatch) {
                matchedPairs++
                flippedTiles.clear()
                isProcessing = false
                checkGameFinished()
                animatorMatch.start()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    runOnUiThread {
                        flippedTiles.forEach { it.revealed = false }
                        flippedTiles.clear()
                        isProcessing = false
                    }
                }, 1000)
                animatorFalse.start()
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

