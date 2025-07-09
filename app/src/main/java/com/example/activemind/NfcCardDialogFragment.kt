package com.example.activemind

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.activemind.databinding.FragmentNfcCardDialogBinding
import com.google.android.material.snackbar.Snackbar


class NfcCardDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentNfcCardDialogBinding
    private lateinit var countDownTimer: CountDownTimer
    private var countDownTimerRemaining: Long = 0L

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentNfcCardDialogBinding.inflate(layoutInflater)

        val name = arguments?.getString("figure") ?: ""
        val imageRes = arguments?.getString("image") ?: ""
        //val data = arguments?.getString("data") ?: ""

        // Mostrar nombre e imagen
        binding.tvNameFragment.text = name
        val resId = requireContext().resources.getIdentifier(imageRes, "drawable", requireContext().packageName)
        binding.ivIconFragment.setImageResource(resId)

        startTimer()

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(false)
            .setPositiveButton("Cerrar") { _, _ -> dismiss() }
            .create()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countDownTimerRemaining = millisUntilFinished
                val secondsLeft = millisUntilFinished / 1000
                binding.tvTimerFragment.text = "Tiempo restante: $secondsLeft s"
            }

            override fun onFinish() {
                dismiss()
            }
        }
        countDownTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }

    companion object {
        fun newInstance(figure: String, imageRes: String, color: String): NfcCardDialogFragment {
            val fragment = NfcCardDialogFragment()
            val args = Bundle().apply {
                putString("figure", figure)
                putString("image", imageRes)
                putString("color", color)
            }
            fragment.arguments = args
            return fragment
        }
    }

    fun checkNfcCard(nfcFigure: String, nfcColor: String){
        val figure = arguments?.getString("figure") ?: ""
        val color = arguments?.getString("color") ?: ""
        val imageRes = arguments?.getString("image") ?: ""

        if (nfcFigure == figure && nfcColor == color) {
            countDownTimer.cancel()
            binding.tvTimerFragment.text = "¡Tarjeta Correcta!"
            binding.tvTimerFragment.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_text_color))

            binding.ivIconFragment.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_64))

            binding.root.postDelayed({
                dismiss()
            }, 1000) // Cierra el dialog tras 1 segundo
        } else {
            binding.ivIconFragment.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_64))
            binding.tvTimerFragment.text = "¡Tarjeta Incorrecta!"
            //Snackbar.make(binding.root, "Tarjeta NFC no válida", Snackbar.LENGTH_SHORT).show()

            binding.root.postDelayed({
                // Restaurar icono original
                val resId = resources.getIdentifier(imageRes, "drawable", requireContext().packageName)
                binding.ivIconFragment.setImageResource(resId)

                // Restaurar texto a contador (sin reiniciar el timer, solo vuelve a mostrar el tiempo actual)
                val millisLeft = countDownTimerRemaining
                val secondsLeft = millisLeft / 1000
                binding.tvTimerFragment.text = "Tiempo restante: $secondsLeft s"

            }, 1000) // Tras 1 segundo, restaurar
        }

    }
}