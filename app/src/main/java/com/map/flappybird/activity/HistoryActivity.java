package com.map.flappybird.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.map.flappybird.R;
import com.map.flappybird.adapter.HistoryAdapter;
import com.map.flappybird.http.HttpClient;
import com.map.flappybird.model.Score;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter historyAdapter;
    private final List<Score> historyList = new ArrayList<>();
    private String userId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String DateConverter(String dateStr) {
        // Parse the date string to a ZonedDateTime object
        ZonedDateTime zonedDateTime = null;
        zonedDateTime = ZonedDateTime.parse(dateStr);

        // Define the desired output format
        DateTimeFormatter formatter = null;
        formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        // Format the ZonedDateTime to a more readable string
        return formatter.format(zonedDateTime);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Lấy userId từ Intent
        userId = getIntent().getStringExtra("userId");
        assert userId != null;
        if (Integer.parseInt(userId) == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RecyclerView recyclerViewHistory = findViewById(R.id.historyRecyclerView);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        historyAdapter = new HistoryAdapter(historyList);
        recyclerViewHistory.setAdapter(historyAdapter);

        // Lấy dữ liệu lịch sử
        fetchHistoryData();
    }

    private void fetchHistoryData() {
        new FetchHistoryTask(this, userId).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchHistoryTask extends AsyncTask<Void, Void, JSONArray> {
        private final Context context;
        private final int userId;

        FetchHistoryTask(Context context, String userId) {
            this.context = context;
            this.userId = Integer.parseInt(userId);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            try {
                HttpClient httpClient = new HttpClient(context);
                return httpClient.getUserHistory(userId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(JSONArray response) {
            if (response != null) {
                try {
                    historyList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject historyObject = response.getJSONObject(i);
                        String username = historyObject.getString("username");
                        int score = historyObject.getInt("score");
                        String createdAt = DateConverter(historyObject.getString("createdAt"));

                        historyList.add(new Score(username, score, createdAt));
                    }
                    historyAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error parsing history data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Failed to fetch history data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
