package com.map.flappybird.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.map.flappybird.R;
import com.map.flappybird.model.Score;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<Score> historyList;

    public HistoryAdapter(List<Score> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_entry, parent, false);
        return new HistoryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Score score = historyList.get(position);
        holder.textPlayerName.setText(score.getUsername());
        holder.textPlayerScore.setText(score.getScore() + " pts");
        holder.textDateTime.setText(score.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textPlayerName, textPlayerScore, textDateTime;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textPlayerName = itemView.findViewById(R.id.text_player_name);
            textPlayerScore = itemView.findViewById(R.id.text_player_score);
            textDateTime = itemView.findViewById(R.id.text_datetime);
        }
    }
}
