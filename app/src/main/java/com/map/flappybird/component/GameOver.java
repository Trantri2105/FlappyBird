package com.map.flappybird.component;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.map.flappybird.R;

public class GameOver {

    private Bitmap gameOver;
    private int screenHeight, screenWidth;

    public GameOver(Resources resources, int screenHeight, int screenWidth) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        gameOver = BitmapFactory.decodeResource(resources, R.drawable.gameover);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(gameOver, screenWidth / 2 - gameOver.getWidth() / 2, screenHeight / 4, null);
    }

    public void update() {

    }
}