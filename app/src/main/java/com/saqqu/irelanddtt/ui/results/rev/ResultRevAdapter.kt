package com.saqqu.irelanddtt.ui.results.rev

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.saqqu.irelanddtt.R
import com.saqqu.irelanddtt.data.models.ResultQuizDataModel

class ResultRevAdapter(private val list: List<ResultQuizDataModel> = ArrayList(), private val context: Context): RecyclerView.Adapter<ResultRevAdapter.ResultRevVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultRevVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_unique_result_rev, parent, false)
        return ResultRevVH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ResultRevVH, position: Int) {
        holder.question.text = list[position].result?.question
        holder.selectedAns.text = list[position].result?.getSelectedOption()
        holder.correctAns.text = list[position].result?.getCorrectOption()
        holder.correctAns.visibility = if (list[position].result?.isSelectedCorrectly() == true) View.GONE else View.VISIBLE
        val backgroundColour = if (list[position].result?.isSelectedCorrectly() == true)
            ContextCompat.getColor(context, R.color.correct_selection)
        else ContextCompat.getColor(context, R.color.wrong_selection)
        holder.cardView.setCardBackgroundColor(backgroundColour)
    }

    class ResultRevVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById(R.id.resItemQuestion)
        val correctAns: TextView = itemView.findViewById(R.id.resItemCorrectAnswer)
        val selectedAns: TextView = itemView.findViewById(R.id.resItemSelected)
        val cardView: CardView = itemView.findViewById(R.id.resCardView)
    }
}