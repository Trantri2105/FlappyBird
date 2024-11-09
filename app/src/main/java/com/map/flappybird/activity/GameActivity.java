package com.map.flappybird.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.map.flappybird.game.GameManager;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        String userId = intent.getStringExtra("USERID");
        GameManager gameManager = new GameManager(this, userId);
        setContentView(gameManager);
    }
}
