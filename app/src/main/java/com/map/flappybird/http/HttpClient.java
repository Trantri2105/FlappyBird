package com.map.flappybird.http;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {

    private final Context context;

    public HttpClient(Context context) {
        this.context = context;
    }

    private void addAuthorizationHeader(HttpURLConnection connection) {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token != null) {
            connection.setRequestProperty("Authorization", "Bearer " + token);
        }
    }

    public JSONObject getRankingData(int page) throws Exception {
        URL url = new URL("http://10.0.2.2:8080/api/ranking?page=" + page);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        addAuthorizationHeader(connection);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JSONObject(response.toString());
        } else {
            return null;
        }
    }

    public boolean postRegisterUser(String urlStr, String username, String password) throws Exception {
        System.out.println("\n####################\n" + "OK I'm here register" + "\n####################\n");
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        String jsonInputString = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_OK;
    }

    public void saveScore(int score, String userId) {
        try {
            URL url = new URL("http://10.0.2.2:8080/api/score");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            addAuthorizationHeader(connection);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);
            String jsonInputString = String.format("{\"user_id\": \"%s\", \"score\": \"%s\"}", userId,score);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (Exception e){
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK){
                System.out.println("Save score failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject postLoginUser(String urlStr, String username, String password) throws Exception {
        System.out.println("\n####################\n" + "OK I'm here login" + "\n####################\n");
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // Tạo JSON yêu cầu
        String jsonInputString = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Kiểm tra phản hồi từ server
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Chuyển đổi phản hồi thành JSON
            return new JSONObject(response.toString());
        } else {
            return null;
        }
    }
}
