package com.example.activemind

import android.app.ComponentCaller
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Rect
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var nfcCardAdapter: NfcCardAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var binding: ActivityMainBinding

    private var success: Int = 0
    private var errors: Int = 0

    //NFC
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Opcional: Muestra el splash por un segundo extra
        splashScreen.setOnExitAnimationListener { splashViewProvider ->
            val splashView = splashViewProvider.view
            splashView.animate()
                .scaleX(2f)
                .scaleY(2f)
                .alpha(0f)
                .setDuration(500L)
                .withEndAction {
                    splashViewProvider.remove()
                }
                .start()
        }

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

        binding.recyclerView.addItemDecoration(SpacesItemDecoration(8))

        //NFC setup
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE)

        binding.btnReset.setOnClickListener {
            errors = 0
            success = 0
            setValuesScore(success, errors)
            nfcCardAdapter.resetAllCards()
        }

        setValuesScore(success, errors)

    }

    private fun setValuesScore(success: Int, errors: Int){
        binding.tvSuccess.text = success.toString()
        binding.tvError.text = errors.toString()
    }

    private fun getCards(): MutableList<NfcCard> {
        val nfcCards = mutableListOf<NfcCard>()

        for (i in 1..10) {
            val nfcCard = when (i) {
                1 -> NfcCard("1", "Círculo", "ic_circle", "Melón")
                2 -> NfcCard("2", "Cuadrado", "ic_square", "Turquesa")
                3 -> NfcCard("3", "Triangulo", "triangulo_ic", "Rosa")
                4 -> NfcCard("4", "Rombo", "ic_diamond", "Gris ")
                5 -> NfcCard("5", "Rectángulo", "ic_rectangle", "Verde")
                6 -> NfcCard("6", "Trapecio", "ic_parallel", "Purpura")
                7 -> NfcCard("7", "Pentágono", "ic_pentagon", "Naranja")
                8 -> NfcCard("8", "Hexágono", "ic_hexagon", "Café")
                9 -> NfcCard("9", "Estrella", "ic_star", "Amarillo")
                10 -> NfcCard("10", "Corazón", "ic_heart", "Rojo")
                else -> null
            }

            nfcCard?.let { nfcCards.add(it) }
        }

        return nfcCards
    }

    override fun onClick(nfcCard: NfcCard, position: Int) {
        val dialog = NfcCardDialogFragment.newInstance(nfcCard.figure, nfcCard.icon, nfcCard.color)
        dialog.show(supportFragmentManager, "NfcCardDialog")
    }

    fun onNfcResult(result: Boolean, figure: String) {
        if (result) {
            success++
            nfcCardAdapter.disableCardByFigure(figure)
        } else {
            errors++
        }
        setValuesScore(success, errors)
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

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)

    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        intent?.let {
            val rawMessages = it.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if(rawMessages != null){
                val messages = rawMessages.map { msg -> msg as NdefMessage }
                for (message in messages) {
                    for (record in message.records) {
                        if (record.tnf == NdefRecord.TNF_WELL_KNOWN &&
                            record.type.contentEquals(NdefRecord.RTD_TEXT)
                        ) {
                            val payload = record.payload
                            val textEncoding = if ((payload[0].toInt() and 0x80) == 0) Charsets.UTF_8 else Charsets.UTF_16
                            val languageCodeLength = payload[0].toInt() and 0x3F
                            val text = payload.copyOfRange(1 + languageCodeLength, payload.size)
                                .toString(textEncoding)

                            try {
                                val json = JSONObject(text)
                                val color = json.getString("color")
                                val figure = json.getString("figure")

                                val dialog = supportFragmentManager.findFragmentByTag("NfcCardDialog") as? NfcCardDialogFragment
                                dialog?.checkNfcCard(figure, color)

                            } catch (e: Exception) {
                                Snackbar.make(binding.root, "Error leyendo tarjeta NFC", Snackbar.LENGTH_SHORT).show()
                                Log.e("NFC", "JSON error", e)
                            }
                        }
                    }
                }
            }else{
                Snackbar.make(binding.root, "No se detecto un mensaje", Snackbar.LENGTH_SHORT).show()
            }

        }
    }
}