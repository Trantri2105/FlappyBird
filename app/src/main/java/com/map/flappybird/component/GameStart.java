package com.map.flappybird.component;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.map.flappybird.R;

public class GameStart {

    private int screenHeight, screenWidth;
    private Bitmap gameStart;

    public GameStart(Resources resources, int screenHeight, int screenWidth) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        gameStart = BitmapFactory.decodeResource(resources, R.drawable.message);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(gameStart, screenWidth / 2 - gameStart.getWidth() / 2, screenHeight / 4, null);
    }
}
