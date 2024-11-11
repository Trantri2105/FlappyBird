package com.map.flappybird.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.map.flappybird.activity.HistoryActivity;
import com.map.flappybird.http.HttpClient;
import com.map.flappybird.model.Score;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryController {

    private final Context context;
    private final HistoryControllerCallback callback;

    public HistoryController(Context context, HistoryControllerCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void fetchUserHistory(int userId) {
        new FetchHistoryTask(userId).execute();
    }

    // Callback interface for data updates
    public interface HistoryControllerCallback {
        void onHistoryDataFetched(List<Score> historyList);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchHistoryTask extends AsyncTask<Void, Void, List<Score>> {
        private final int userId;

        FetchHistoryTask(int userId) {
            this.userId = userId;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected List<Score> doInBackground(Void... voids) {
            try {
                HttpClient httpClient = new HttpClient(context);
                JSONArray response = httpClient.getUserHistory(userId);
                List<Score> historyList = new ArrayList<>();

                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject historyObject = response.getJSONObject(i);
                        String username = historyObject.getString("username");
                        int score = historyObject.getInt("score");
                        String createdAt = HistoryActivity.DateConverter(historyObject.getString("createdAt"));
                        historyList.add(new Score(username, score, createdAt));
                    }
                    return historyList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Score> historyList) {
            if (historyList != null) {
                callback.onHistoryDataFetched(historyList);
            } else {
                Toast.makeText(context, "Failed to fetch history data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
