package pl.wsei.pam.lab03

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
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
    private var iconsToUse: MutableList<Int> = mutableListOf()
    private var matchedPairs = 0
    private val flippedTiles = mutableListOf<Tile>()
    private lateinit var logic: MemoryGameLogic
    private var isProcessing = false

    lateinit var completionPlayer: MediaPlayer
    lateinit var negativePlayer: MediaPlayer
    private var savedRevealedStates: ArrayList<Boolean>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab03)

        gridLayout = findViewById(R.id.gridLayout)

        if (savedInstanceState != null) {
            iconsToUse = savedInstanceState.getIntegerArrayList("iconsToUse")!!.toMutableList()
            matchedPairs = savedInstanceState.getInt("matchedPairs", 0)
            savedRevealedStates = savedInstanceState.getSerializable("revealedStates") as ArrayList<Boolean>
        }


        val size = intent.getIntArrayExtra("size") ?: intArrayOf(4, 4)
        val rows = size[0]
        val columns = size[1]

        logic = MemoryGameLogic(getPairsCount(rows, columns))

        if (savedInstanceState != null) {
            iconsToUse = savedInstanceState.getIntegerArrayList("iconsToUse")!!.toMutableList()
            matchedPairs = savedInstanceState.getInt("matchedPairs", 0)
        } else {
            val allIcons = listOf(
                R.drawable.baseline_rocket_launch_24, R.drawable.sharp_accessibility_24, R.drawable.s1, R.drawable.s2,
                R.drawable.s3, R.drawable.s4, R.drawable.s5, R.drawable.s6, R.drawable.s7, R.drawable.s8,
                R.drawable.s9, R.drawable.s10, R.drawable.s11, R.drawable.s12, R.drawable.s13, R.drawable.s14,
                R.drawable.s15, R.drawable.s16
            )
            val numPairs = logic.maxMatches
            iconsToUse = (allIcons.take(numPairs) + allIcons.take(numPairs)).shuffled().toMutableList()
        }

        setupBoard(columns, rows)
    }

    private fun getPairsCount(rows: Int, columns: Int): Int {
        return when {
            columns == 2 && rows == 3 -> 3
            columns == 3 && rows == 4 -> 6
            columns == 4 && rows == 4 -> 8
            columns == 6 && rows == 6 -> 18
            else -> throw IllegalArgumentException("Invalid board size")
        }
    }

    private fun setupBoard(columns: Int, rows: Int) {
        gridLayout.columnCount = columns
        gridLayout.removeAllViews()

        val iconsCopy = iconsToUse.toMutableList()

        var index = 0
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val icon = iconsCopy.removeAt(0)
                val button = ImageButton(gridLayout.context).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        rowSpec = GridLayout.spec(i, 1f)
                        columnSpec = GridLayout.spec(j, 1f)
                        setMargins(10, 10, 10, 10)
                    }
                    tag = "$i,$j"
                }

                val tile = Tile(button, icon, R.drawable.deck)
                tiles[button.tag.toString()] = tile

                // Przywrócenie stanu
                savedRevealedStates?.let { states ->
                    tile.revealed = states[index]
                }
                index++

                button.setOnClickListener { onTileClicked(tile) }
                gridLayout.addView(button)
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
                playMatchAnimation(flippedTiles[0], flippedTiles[1])
                completionPlayer.start()
                flippedTiles.clear()
                isProcessing = false
                checkGameFinished()
            } else {
                playNoMatchAnimation(flippedTiles[0], flippedTiles[1])
                negativePlayer.start()
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

    private fun playMatchAnimation(tile1: Tile, tile2: Tile) {
        val animators = listOf(tile1, tile2).flatMap { tile ->
            listOf(
                ObjectAnimator.ofFloat(tile.button, "scaleX", 1f, 1.2f, 1f),
                ObjectAnimator.ofFloat(tile.button, "scaleY", 1f, 1.2f, 1f),
                ObjectAnimator.ofFloat(tile.button, "rotation", 0f, 360f),
                ObjectAnimator.ofFloat(tile.button, "alpha", 1f, 0.3f, 1f)
            )
        }
        AnimatorSet().apply {
            playTogether(animators)
            duration = 600L
            start()
        }
    }

    private fun playNoMatchAnimation(tile1: Tile, tile2: Tile) {
        val animators = listOf(tile1, tile2).flatMap { tile ->
            listOf(
                ObjectAnimator.ofFloat(tile.button, "translationX", 0f, 20f, -20f, 0f),
                ObjectAnimator.ofFloat(tile.button, "translationY", 0f, 20f, -20f, 0f)
            )
        }
        AnimatorSet().apply {
            playTogether(animators)
            duration = 400L
            start()
        }
    }

    private fun checkGameFinished() {
        if (matchedPairs == logic.maxMatches) {
            Toast.makeText(this, "Game finished!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        completionPlayer = MediaPlayer.create(applicationContext, R.raw.completion)
        negativePlayer = MediaPlayer.create(applicationContext, R.raw.negative_guitar)
    }

    override fun onPause() {
        super.onPause()
        completionPlayer.release()
        negativePlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("iconsToUse", ArrayList(iconsToUse))
        outState.putInt("matchedPairs", matchedPairs)

        val revealedStates = ArrayList<Boolean>()
        tiles.entries.sortedBy { it.key }.forEach { revealedStates.add(it.value.revealed) }
        outState.putSerializable("revealedStates", revealedStates)
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
}
