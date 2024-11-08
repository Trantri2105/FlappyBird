package com.map.flappybird.activity;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.map.flappybird.game.GameManager;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        GameManager gameManager = new GameManager(this);
        setContentView(gameManager);
        gameManager.setUserId("abcdxyz");
    }
}
