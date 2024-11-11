package com.map.flappybird.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.map.flappybird.R;
import com.map.flappybird.adapter.LeaderboardAdapter;
import com.map.flappybird.http.HttpClient;
import com.map.flappybird.model.Score;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private LeaderboardAdapter leaderboardAdapter;
    private final List<Score> scoreList = new ArrayList<>();
    private int currentPage = 0;
    private int totalPages = 1;

    // Top 3 người chơi
    private Score topFirstPlace;
    private Score topSecondPlace;
    private Score topThirdPlace;

    private TextView textFirstPlaceName;
    private TextView textSecondPlaceName;
    private TextView textThirdPlaceName;

    private TextView textFirstPlaceScore;
    private TextView textSecondPlaceScore;
    private TextView textThirdPlaceScore;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String DateConverter(String dateStr) {
        // Parse the date string to a ZonedDateTime object
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr);

        // Chuyển đổi sang múi giờ GMT+7
        ZonedDateTime gmtPlus7DateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("GMT+7"));

        // Define the desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        // Format the ZonedDateTime to a more readable string
        return formatter.format(gmtPlus7DateTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // Khởi tạo các TextView cho người chơi top 3
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
                fetchRankingData();
            }
        });

        buttonPrevious.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                fetchRankingData();
            }
        });

        // Lấy dữ liệu top 3 khi mở activity
        fetchTopThreeData();
        fetchRankingData(); // Lấy dữ liệu cho trang hiện tại
    }

    private void fetchTopThreeData() {
        // Lấy top 3 từ trang đầu tiên
        new FetchTopThreeTask(this).execute();
    }

    private void fetchRankingData() {
        new FetchRankingTask(this, currentPage).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchTopThreeTask extends AsyncTask<Void, Void, JSONObject> {
        private final Context context;

        FetchTopThreeTask(Context context) {
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                HttpClient httpClient = new HttpClient(context);
                return httpClient.getRankingData(0); // Lấy dữ liệu từ trang đầu
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    JSONArray scoresArray = response.getJSONArray("scores");

                    if (scoresArray.length() > 0) {
                        topFirstPlace = new Score(
                                scoresArray.getJSONObject(0).getString("username"),
                                scoresArray.getJSONObject(0).getInt("score"),
                                scoresArray.getJSONObject(0).getString("createdAt"),
                                scoresArray.getJSONObject(0).getInt("userId"));
                    }
                    if (scoresArray.length() > 1) {
                        topSecondPlace = new Score(
                                scoresArray.getJSONObject(1).getString("username"),
                                scoresArray.getJSONObject(1).getInt("score"),
                                scoresArray.getJSONObject(1).getString("createdAt"),
                                scoresArray.getJSONObject(0).getInt("userId"));
                    }
                    if (scoresArray.length() > 2) {
                        topThirdPlace = new Score(
                                scoresArray.getJSONObject(2).getString("username"),
                                scoresArray.getJSONObject(2).getInt("score"),
                                scoresArray.getJSONObject(2).getString("createdAt"),
                                scoresArray.getJSONObject(0).getInt("userId"));
                    }

                    // Hiển thị top 3 người chơi
                    if (topFirstPlace != null) {
                        textFirstPlaceName.setText(topFirstPlace.getUsername());
                        textFirstPlaceScore.setText(topFirstPlace.getScore() + " pts");
                    }
                    if (topSecondPlace != null) {
                        textSecondPlaceName.setText(topSecondPlace.getUsername());
                        textSecondPlaceScore.setText(topSecondPlace.getScore() + " pts");
                    }
                    if (topThirdPlace != null) {
                        textThirdPlaceName.setText(topThirdPlace.getUsername());
                        textThirdPlaceScore.setText(topThirdPlace.getScore() + " pts");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error parsing top 3 data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Failed to fetch top 3 data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchRankingTask extends AsyncTask<Void, Void, JSONObject> {
        private final Context context;
        private final int page;

        FetchRankingTask(Context context, int page) {
            this.context = context;
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
        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    // Cập nhật dữ liệu bảng xếp hạng cho trang hiện tại
                    totalPages = response.getInt("totalPages");
                    JSONArray scoresArray = response.getJSONArray("scores");

                    scoreList.clear();
                    for (int i = 0; i < scoresArray.length(); i++) {
                        JSONObject scoreObject = scoresArray.getJSONObject(i);
                        String username = scoreObject.getString("username");
                        int score = scoreObject.getInt("score");
                        String createdAt = DateConverter(scoreObject.getString("createdAt"));
                        int userId = scoreObject.getInt("userId");
//                        convertToReadableFormat(scoreObject.getString("createdAt"))

                        scoreList.add(new Score(username, score, createdAt, userId));
                    }

                    leaderboardAdapter.notifyDataSetChanged();

                    // Quản lý trạng thái của nút "Tiếp" và "Trước"
                    Button buttonNext = findViewById(R.id.button_next);
                    Button buttonPrevious = findViewById(R.id.button_previous);

                    // Bật hoặc tắt nút "Tiếp" và "Trước" dựa trên số trang hiện tại
                    buttonNext.setEnabled(currentPage < totalPages - 1);
                    buttonPrevious.setEnabled(currentPage > 0);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


