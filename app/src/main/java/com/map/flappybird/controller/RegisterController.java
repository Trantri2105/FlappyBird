package com.map.flappybird.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.map.flappybird.activity.LoginActivity;
import com.map.flappybird.http.HttpClient;


public class RegisterController {

    public void registerUser(String username, String password, Context context) {
        new RegisterTask(context).execute(username, password);
    }

    private static class RegisterTask extends AsyncTask<String, Void, Boolean> {
        @SuppressLint("StaticFieldLeak")
        private final Context context;

        RegisterTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            try {
                HttpClient httpClient = new HttpClient(context);
                System.out.println("\n####################\n" + "OK I'm here" + "\n####################\n");
                return httpClient.postRegisterUser("http://10.0.2.2:8080/api/register", username, password);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (isSuccess) {
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



