package com.saqqu.irelanddtt.ui.dtt.rev

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.saqqu.irelanddtt.R


class DTTRevAdapter(
    private val context: Context,
    private var list: List<String>,
    private val listener: DTTRevListener,
    private var selectedPosition: Int
): RecyclerView.Adapter<DTTRevAdapter.OptionsVH>() {

    fun reFeedData(list: List<String>) {
        this.list = list
    }

    fun setSelectedItem(selectedPosition: Int) {
        val tempSelection = this.selectedPosition
        this.selectedPosition = selectedPosition
        if (tempSelection >= 0) {
            notifyItemChanged(tempSelection)
        }

        if (selectedPosition >= 0) notifyItemChanged(selectedPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsVH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_quiz_options, parent, false)
        return OptionsVH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OptionsVH, position: Int) {
        holder.optionTxt.text = list[position]
        holder.itemView.setOnClickListener {
            listener.onOptionSelected(position, list[position])
            this.selectedPosition = position
        }
        val backgroundColour = if (this.selectedPosition == position)
            ContextCompat.getColor(context, R.color.primary)
        else ContextCompat.getColor(context, R.color.primaryDark)


        holder.optionCard.setCardBackgroundColor(backgroundColour)
    }

    class OptionsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val optionTxt: TextView = itemView.findViewById(R.id.quizOptionTxt)
        val optionCard: CardView = itemView.findViewById(R.id.dttOptionCard)
    }
}