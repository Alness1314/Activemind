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
        gridLayoutManager = GridLayoutManager(this, 3) // ← Grid de 3 columnas

        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = nfcCardAdapter
        }

        binding.recyclerView.addItemDecoration(
            SpacesItemDecoration(16) // 16px o usa `resources.getDimensionPixelSize(R.dimen.spacing)`
        )
    }

    fun getCards(): MutableList<NfcCard>{
        return mutableListOf();
    }

    override fun onClick(nfcCard: NfcCard, position: Int) {
        Toast.makeText(this, "Clicked: " + nfcCard.name, Toast.LENGTH_SHORT).show();
    }

    class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space
        }
    }
}