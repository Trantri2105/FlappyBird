package com.map.flappybird.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.map.flappybird.R;
import com.map.flappybird.adapter.HistoryAdapter;
import com.map.flappybird.controller.HistoryController;
import com.map.flappybird.model.Score;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryController.HistoryControllerCallback {

    private HistoryAdapter historyAdapter;
    private final List<Score> historyList = new ArrayList<>();

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
        setContentView(R.layout.activity_history);

        String userId = getIntent().getStringExtra("userId");
        if (userId == null || Integer.parseInt(userId) == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RecyclerView recyclerViewHistory = findViewById(R.id.historyRecyclerView);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        historyAdapter = new HistoryAdapter(historyList);
        recyclerViewHistory.setAdapter(historyAdapter);

        // Initialize HistoryController and fetch history data
        HistoryController historyController = new HistoryController(this, this);
        historyController.fetchUserHistory(Integer.parseInt(userId));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onHistoryDataFetched(List<Score> historyList) {
        this.historyList.clear();
        this.historyList.addAll(historyList);
        historyAdapter.notifyDataSetChanged();
    }
}
