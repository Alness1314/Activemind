package com.example.activemind

import android.app.AlertDialog
import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.AnimationUtils
import androidx.annotation.RawRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.activemind.MainActivity
import com.example.activemind.R
import com.example.activemind.databinding.FragmentNfcCardDialogBinding
import org.json.JSONObject


class NfcCardDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentNfcCardDialogBinding
    private lateinit var countDownTimer: CountDownTimer
    private var countDownTimerRemaining: Long = 0L

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentNfcCardDialogBinding.inflate(layoutInflater)

        val name = arguments?.getString("figure") ?: ""
        val imageRes = arguments?.getString("image") ?: ""

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

        val result = nfcFigure == figure && nfcColor == color

        if (result) {
            countDownTimer.cancel()

            binding.tvTimerFragment.text = "¡Figura Correcta!"
            binding.tvTimerFragment.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_text_color))
            binding.ivIconFragment.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_64))

            val scale = AnimationUtils.loadAnimation(requireContext(), R.anim.scale)
            binding.ivIconFragment.startAnimation(scale)

            playSound(R.raw.sound_correct)

            /*binding.root.postDelayed({
                dismiss()
            }, 1000)*/
        } else {

            binding.tvTimerFragment.text = "¡Figura Incorrecta!"
            binding.ivIconFragment.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_64))
            playSound(R.raw.sound_wrong)

            val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
            binding.ivIconFragment.startAnimation(shake)

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

        // Comunicar resultado al activity
        (activity as? MainActivity)?.onNfcResult(result, figure)

        if (result) {
            binding.root.postDelayed({
                dismiss()
            }, 1000)
        }

    }

    private fun playSound(@RawRes soundResId: Int) {
        val mediaPlayer = MediaPlayer.create(requireContext(), soundResId)
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.start()
    }
}