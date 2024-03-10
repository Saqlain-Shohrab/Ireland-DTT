package com.saqqu.irelanddtt.ui.results.rev

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.saqqu.irelanddtt.R
import com.saqqu.irelanddtt.data.models.ResultModel

class ResultsRevAdapter(
    private val context: Context,
    var list: List<ResultModel>,
    private val clickListener: ResultsToResultListener
): RecyclerView.Adapter<ResultsRevAdapter.ResultsRevVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsRevVH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_result_rev, parent, false)
        return ResultsRevVH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ResultsRevVH, position: Int) {
        holder.titleTextView.text = list[position].date.toString()
        val scoreDivision = list[position].getScoreDivision()
        holder.scoreTextView.text = "${scoreDivision}Total: ${list[position].scored}/${list[position].outOf}"
        holder.qualifyStatusTextView.text = if(list[position].scored >= 0.875 * list[position].outOf)  "Pass" else "Fail"
        //if(list[position].scored >= 0.875 * list[position].outOf)  holder.cardLay.setBackgroundColor(Color.GREEN) else holder.cardLay.setBackgroundColor(Color.RED)
        holder.itemView.setOnClickListener {
            clickListener.onResultClicked(list[position])
        }
    }

    fun setData(results: MutableList<ResultModel>) {
        this.list = results
    }


    class ResultsRevVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.resultTitleMetaData)
        val scoreTextView: TextView = itemView.findViewById(R.id.resultScoreMetaData)
        val qualifyStatusTextView: TextView = itemView.findViewById(R.id.resultQualifyMetaData)
    }
}