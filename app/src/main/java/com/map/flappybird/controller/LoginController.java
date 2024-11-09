package com.map.flappybird.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.map.flappybird.activity.MenuActivity;
import com.map.flappybird.http.HttpClient;

import org.json.JSONObject;

public class LoginController {

    public void loginUser(String username, String password, Context context) {
        new LoginTask(context).execute(username, password);
    }

    private static class LoginTask extends AsyncTask<String, Void, JSONObject> {
        @SuppressLint("StaticFieldLeak")
        private final Context context;

        LoginTask(Context context) {
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            try {
                HttpClient httpClient = new HttpClient(context);
                return httpClient.postLoginUser("http://10.0.2.2:8080/api/login", username, password);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    // Nhận userId và username từ phản hồi JSON
                    String userId = response.getString("userId");
                    String username = response.getString("username");
                    String token = response.getString("token");

                    // Lưu token vào SharedPreferences
                    SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    prefs.edit().putString("token", token).apply();

                    // Hiển thị thông báo đăng nhập thành công
                    Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show();

                    // Truyền userId và username vào Intent
                    Intent intent = new Intent(context, MenuActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("username", username);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


