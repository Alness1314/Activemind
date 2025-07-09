package com.example.activemind

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activemind.adapters.NfcCardAdapter
import com.example.activemind.databinding.ActivityMainBinding
import com.example.activemind.dto.NfcCard
import com.example.activemind.interfaces.OnClickListener

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var nfcCardAdapter: NfcCardAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nfcCardAdapter = NfcCardAdapter(getCards(), this)
        gridLayoutManager = GridLayoutManager(this, 2) // ← Grid de 3 columnas

        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = nfcCardAdapter
        }

        binding.recyclerView.addItemDecoration(
            SpacesItemDecoration(8) // 16px o usa `resources.getDimensionPixelSize(R.dimen.spacing)`
        )
    }

    private fun getCards(): MutableList<NfcCard> {
        val nfcCards = mutableListOf<NfcCard>()

        for (i in 1..10) {
            val nfcCard = when (i) {
                1 -> NfcCard(
                    "1",
                    "Circle",
                    "ic_circle",
                    "Red"
                )

                2 -> NfcCard(
                    "2",
                    "Square",
                    "ic_square",
                    "Blue"
                )

                3 -> NfcCard(
                    "3",
                    "Triangle",
                    "ic_triangle",
                    "Yellow"
                )

                4 -> NfcCard(
                    "4",
                    "Pentagon",
                    "ic_pentagon",
                    "Green"
                )

                5 -> NfcCard(
                    "5",
                    "Hexagon",
                    "ic_hexagon",
                    "Purple"
                )

                6 -> NfcCard(
                    "6",
                    "Rectangle",
                    "ic_rectangle",
                    "Orange"
                )

                7 -> NfcCard(
                    "7",
                    "Star",
                    "ic_star",
                    "Gold"
                )

                8 -> NfcCard(
                    "8",
                    "Moon",
                    "ic_moon",
                    "Sky blue"
                )

                9 -> NfcCard(
                    "9",
                    "Sunny",
                    "ic_sunny",
                    "Grey"
                )

                10 -> NfcCard(
                    "10",
                    "Heart",
                    "ic_heart",
                    "Pink"
                )

                else -> null
            }

            nfcCard?.let { nfcCards.add(it) }
        }

        return nfcCards
    }

    override fun onClick(nfcCard: NfcCard, position: Int) {
        val dialog = NfcCardDialogFragment.newInstance(nfcCard.name, nfcCard.icon, nfcCard.data)
        dialog.show(supportFragmentManager, "NfcCardDialog")
    }

    class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space
        }
    }
}