package com.saqqu.irelanddtt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.saqqu.irelanddtt.data.models.QuizDataModel;

import java.util.ArrayList;
import java.util.List;

public class RevAdapter extends RecyclerView.Adapter<RevAdapter.RevVH> {

    List<QuizDataModel> questions = new ArrayList<>();
    Context context;
    public RevAdapter(List<QuizDataModel> questions, Context context) {
        this.questions = questions;
        this.context = context;
    }
    @NonNull
    @Override
    public RevVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rev, parent, false);
        return new RevVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RevVH holder, int position) {
        QuizDataModel model = questions.get(position);
        holder.question.setText(model.question);
        StringBuilder options = new StringBuilder();
        for (String appender : model.getShuffledOptions()) {
            options.append(appender).append("\n");
        }
        holder.options.setText(options);
        holder.explain.setText(model.explanation);
        holder.qType.setText(model.type);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class RevVH extends RecyclerView.ViewHolder{
        TextView question, options, explain, qType;
        public RevVH(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            options = itemView.findViewById(R.id.options);
            explain = itemView.findViewById(R.id.explanation);
            qType = itemView.findViewById(R.id.type_q);
        }
    }
}
