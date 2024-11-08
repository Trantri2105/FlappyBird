package com.map.flappybird.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.map.flappybird.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu); // Chỉ định file XML layout

        // Lấy tham chiếu đến TextView
        TextView welcomeMessage = findViewById(R.id.welcomeMessage);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId"); // lấy userId từ Intent
        String username = intent.getStringExtra("username"); // lấy username từ Intent

        // Hiển thị message chào mừng
        String welcomeText = "Welcome, " + username + " (ID: " + userId + ")";
        welcomeMessage.setText(welcomeText);

        // Xử lý sự kiện các nút bấm
        findViewById(R.id.playButton).setOnClickListener(v -> openPlay());
        findViewById(R.id.rankingButton).setOnClickListener(v -> openRanking());
        findViewById(R.id.historyButton).setOnClickListener(v -> openHistory());
    }

    private void openPlay() {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
    }

    private void openRanking() {
        // Mở trang RankingActivity
//        Intent intent = new Intent(HomeActivity.this, RankingActivity.class);
//        startActivity(intent);
    }

    private void openHistory() {
        // Mở trang HistoryActivity
//        Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
//        startActivity(intent);
    }
}

