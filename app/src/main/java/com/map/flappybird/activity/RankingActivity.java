package com.map.flappybird.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.map.flappybird.R;
import com.map.flappybird.adapter.LeaderboardAdapter;
import com.map.flappybird.controller.RankingController;
import com.map.flappybird.model.Score;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity implements RankingController.RankingControllerCallback {

    private LeaderboardAdapter leaderboardAdapter;
    private final List<Score> scoreList = new ArrayList<>();
    private int currentPage = 0;
    private int totalPages = 1;

    private TextView textFirstPlaceName;
    private TextView textSecondPlaceName;
    private TextView textThirdPlaceName;
    private TextView textFirstPlaceScore;
    private TextView textSecondPlaceScore;
    private TextView textThirdPlaceScore;

    private RankingController rankingController;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String DateConverter(String dateStr) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr);
        ZonedDateTime gmtPlus7DateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("GMT+7"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        return formatter.format(gmtPlus7DateTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        textFirstPlaceName = findViewById(R.id.text_first_place_name);
        textSecondPlaceName = findViewById(R.id.text_second_place_name);
        textThirdPlaceName = findViewById(R.id.text_third_place_name);

        textFirstPlaceScore = findViewById(R.id.text_first_place_score);
        textSecondPlaceScore = findViewById(R.id.text_second_place_score);
        textThirdPlaceScore = findViewById(R.id.text_third_place_score);

        RecyclerView recyclerViewLeaderboard = findViewById(R.id.recycler_view_leaderboard);
        recyclerViewLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        leaderboardAdapter = new LeaderboardAdapter(scoreList);
        recyclerViewLeaderboard.setAdapter(leaderboardAdapter);

        Button buttonNext = findViewById(R.id.button_next);
        Button buttonPrevious = findViewById(R.id.button_previous);

        buttonNext.setOnClickListener(v -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                rankingController.fetchRankingData(currentPage);
            }
        });

        buttonPrevious.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                rankingController.fetchRankingData(currentPage);
            }
        });

        rankingController = new RankingController(this, this);
        rankingController.fetchTopThreeData();
        rankingController.fetchRankingData(currentPage);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTopThreeFetched(List<Score> topScores) {
        if (!topScores.isEmpty()) {
            textFirstPlaceName.setText(topScores.get(0).getUsername());
            textFirstPlaceScore.setText(topScores.get(0).getScore() + " pts");
        }
        if (topScores.size() > 1) {
            textSecondPlaceName.setText(topScores.get(1).getUsername());
            textSecondPlaceScore.setText(topScores.get(1).getScore() + " pts");
        }
        if (topScores.size() > 2) {
            textThirdPlaceName.setText(topScores.get(2).getUsername());
            textThirdPlaceScore.setText(topScores.get(2).getScore() + " pts");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRankingDataFetched(List<Score> scores, int totalPages) {
        this.totalPages = totalPages;
        scoreList.clear();
        scoreList.addAll(scores);
        leaderboardAdapter.notifyDataSetChanged();

        Button buttonNext = findViewById(R.id.button_next);
        Button buttonPrevious = findViewById(R.id.button_previous);
        buttonNext.setEnabled(currentPage < totalPages - 1);
        buttonPrevious.setEnabled(currentPage > 0);
    }
}
