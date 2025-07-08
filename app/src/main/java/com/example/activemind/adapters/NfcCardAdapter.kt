package com.example.activemind.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.activemind.R
import com.example.activemind.databinding.ItemCardBinding
import com.example.activemind.dto.NfcCard
import com.example.activemind.interfaces.OnClickListener

class NfcCardAdapter(private val nfcCards: List<NfcCard>, private val listener: OnClickListener): RecyclerView.Adapter<NfcCardAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nfcCard = nfcCards[position]

        with(holder){
            setListener(nfcCard, position)
            binding.tvTitle.text = nfcCard.name

            val resId = context.resources.getIdentifier(nfcCard.icon, "drawable", context.packageName)

            Glide.with(context)
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(binding.ivIcon);
        }
    }

    override fun getItemCount(): Int = nfcCards.size


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemCardBinding.bind(view)

        fun setListener(nfcCard: NfcCard, position: Int){
            binding.root.setOnClickListener{
                listener.onClick(nfcCard, position)
            }
        }
    }


}