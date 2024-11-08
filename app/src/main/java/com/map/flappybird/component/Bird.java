package com.map.flappybird.component;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.map.flappybird.R;

public class Bird {

    private Bitmap bird_down;
    private Bitmap bird_up;
    private int birdX, birdY, birdWidth, birdHeight;
    private float gravity;
    private float currentFallingSpeed;
    private float jumpBoost;
    private boolean collision = false;
    private int screenHeight;
    private Rect birdPosition;

    public Rect getBirdPosition() {
        return birdPosition;
    }

    public Bird(Resources resources, int screenHeight) {
        this.screenHeight = screenHeight;

        birdX = (int) resources.getDimension(R.dimen.bird_x);
        birdY = screenHeight / 2;
        birdWidth = (int) resources.getDimension(R.dimen.bird_width);
        birdHeight = (int) resources.getDimension(R.dimen.bird_height);
        gravity = resources.getDimension(R.dimen.gravity);
        jumpBoost = resources.getDimension(R.dimen.flappy_boost);

        Bitmap birdBmpDown = BitmapFactory.decodeResource(resources, R.drawable.bird_down);
        bird_down = Bitmap.createScaledBitmap(birdBmpDown, birdWidth, birdHeight, false);
        Bitmap birdBmpUp = BitmapFactory.decodeResource(resources, R.drawable.bird_up);
        bird_up = Bitmap.createScaledBitmap(birdBmpUp, birdWidth, birdHeight, false);
    }

    public void draw(Canvas canvas) {
        if (currentFallingSpeed < 0) {
            canvas.drawBitmap(bird_up, birdX, birdY, null);
        } else {
            canvas.drawBitmap(bird_down, birdX, birdY, null);
        }
    }

    public void update() {
        if(collision) {
            if(birdY + bird_down.getHeight() < screenHeight) {
                birdY += currentFallingSpeed;
                currentFallingSpeed += gravity;
            }
        } else {
            birdY += currentFallingSpeed;
            currentFallingSpeed += gravity;
            birdPosition = new Rect(birdX, birdY, birdX + birdWidth, birdY + birdHeight);
        }
    }

    public void onTouchEvent() {
        if(!collision) {
            currentFallingSpeed = jumpBoost;
        }
    }

    public void collision() {
        collision = true;
    }
}