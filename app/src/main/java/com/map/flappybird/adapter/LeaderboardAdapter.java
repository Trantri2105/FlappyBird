package com.map.flappybird.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.map.flappybird.R;
import com.map.flappybird.activity.HistoryActivity;
import com.map.flappybird.model.Score;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private final List<Score> scoreList;

    public LeaderboardAdapter(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard_entry, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        Score score = scoreList.get(position);
        holder.usernameTextView.setText("User: " + score.getUsername());
        holder.scoreTextView.setText(score.getScore() + " pts");
        holder.dateTextView.setText(score.getCreatedAt());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HistoryActivity.class);
            intent.putExtra("userId", score.getUserId() + ""); // Lấy userId của người chơi được nhấn
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, scoreTextView, dateTextView;

        LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.text_player_name);
            scoreTextView = itemView.findViewById(R.id.text_player_score);
            dateTextView = itemView.findViewById(R.id.text_date);
        }
    }
}
