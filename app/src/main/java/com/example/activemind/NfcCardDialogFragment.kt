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


class NfcCardDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentNfcCardDialogBinding
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentNfcCardDialogBinding.inflate(layoutInflater)

        val name = arguments?.getString("name") ?: ""
        val imageRes = arguments?.getString("image") ?: ""
        val data = arguments?.getString("data") ?: ""

        // Mostrar nombre e imagen
        binding.tvNameFragment.text = name
        val resId = requireContext().resources.getIdentifier(imageRes, "drawable", requireContext().packageName)
        binding.ivIcon.setImageResource(resId)

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
        fun newInstance(name: String, imageRes: String, data: String): NfcCardDialogFragment {
            val fragment = NfcCardDialogFragment()
            val args = Bundle().apply {
                putString("name", name)
                putString("image", imageRes)
                putString("data", data)
            }
            fragment.arguments = args
            return fragment
        }
    }

    fun checkNfcCard(nfcName: String, nfcData: String){
        val name = arguments?.getString("name") ?: ""
        val data = arguments?.getString("data") ?: ""

        if (nfcName == name && nfcData == data) {
            countDownTimer.cancel()
            binding.tvTimerFragment.text = "¡Tarjeta Correcta!"
            binding.tvTimerFragment.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            binding.root.postDelayed({
                dismiss()
            }, 1500) // Cierra el dialog tras 1.5 segundos
        } else {
            Toast.makeText(requireContext(), "Tarjeta NFC no válida", Toast.LENGTH_SHORT).show()
        }

    }
}