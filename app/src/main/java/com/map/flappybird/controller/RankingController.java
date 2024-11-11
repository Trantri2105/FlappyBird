package com.map.flappybird.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.map.flappybird.activity.RankingActivity;
import com.map.flappybird.http.HttpClient;
import com.map.flappybird.model.Score;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RankingController {

    private final Context context;
    private final RankingControllerCallback callback;

    public RankingController(Context context, RankingControllerCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void fetchTopThreeData() {
        new FetchTopThreeTask().execute();
    }

    public void fetchRankingData(int page) {
        new FetchRankingTask(page).execute();
    }

    // Callback interface for data updates
    public interface RankingControllerCallback {
        void onTopThreeFetched(List<Score> topScores);
        void onRankingDataFetched(List<Score> scores, int totalPages);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchTopThreeTask extends AsyncTask<Void, Void, List<Score>> {

        @Override
        protected List<Score> doInBackground(Void... voids) {
            try {
                HttpClient httpClient = new HttpClient(context);
                JSONObject response = httpClient.getRankingData(0);
                if (response != null) {
                    JSONArray scoresArray = response.getJSONArray("scores");
                    List<Score> topScores = new ArrayList<>();

                    for (int i = 0; i < Math.min(3, scoresArray.length()); i++) {
                        JSONObject scoreObject = scoresArray.getJSONObject(i);
                        String username = scoreObject.getString("username");
                        int score = scoreObject.getInt("score");
                        String createdAt = scoreObject.getString("createdAt");
                        int userId = scoreObject.getInt("userId");
                        topScores.add(new Score(username, score, createdAt, userId));
                    }
                    return topScores;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Score> topScores) {
            if (topScores != null) {
                callback.onTopThreeFetched(topScores);
            } else {
                Toast.makeText(context, "Failed to fetch top 3 data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchRankingTask extends AsyncTask<Void, Void, JSONObject> {
        private final int page;

        FetchRankingTask(int page) {
            this.page = page;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                HttpClient httpClient = new HttpClient(context);
                return httpClient.getRankingData(page);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    int totalPages = response.getInt("totalPages");
                    JSONArray scoresArray = response.getJSONArray("scores");

                    List<Score> scores = new ArrayList<>();
                    for (int i = 0; i < scoresArray.length(); i++) {
                        JSONObject scoreObject = scoresArray.getJSONObject(i);
                        String username = scoreObject.getString("username");
                        int score = scoreObject.getInt("score");
                        String createdAt = RankingActivity.DateConverter(scoreObject.getString("createdAt"));
                        int userId = scoreObject.getInt("userId");

                        scores.add(new Score(username, score, createdAt, userId));
                    }
                    callback.onRankingDataFetched(scores, totalPages);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error parsing ranking data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Failed to fetch ranking data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
