package com.map.flappybird.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.map.flappybird.R;
import com.map.flappybird.http.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;

public class Score {

    private static final String SCORE_PREF = "Score pref";
    private Bitmap zero;
    private Bitmap one;
    private Bitmap two;
    private Bitmap three;
    private Bitmap four;
    private Bitmap five;
    private Bitmap six;
    private Bitmap seven;
    private Bitmap eight;
    private Bitmap nine;
    private Bitmap bmpScore;
    private HashMap<Integer, Bitmap> map = new HashMap<>();
    private int screenHeight, screenWidth;
    private int score;
    private boolean collision = false;
    private HttpClient httpClient;


    public Score(Resources resources, int screenHeight, int screenWidth, Context context) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.httpClient = new HttpClient(context);
        bmpScore = BitmapFactory.decodeResource(resources, R.drawable.score);
        zero = BitmapFactory.decodeResource(resources, R.drawable.zero);
        one = BitmapFactory.decodeResource(resources, R.drawable.one);
        two = BitmapFactory.decodeResource(resources, R.drawable.two);
        three = BitmapFactory.decodeResource(resources, R.drawable.three);
        four = BitmapFactory.decodeResource(resources, R.drawable.four);
        five = BitmapFactory.decodeResource(resources, R.drawable.five);
        six = BitmapFactory.decodeResource(resources, R.drawable.six);
        seven = BitmapFactory.decodeResource(resources, R.drawable.seven);
        eight = BitmapFactory.decodeResource(resources, R.drawable.eight);
        nine = BitmapFactory.decodeResource(resources, R.drawable.nine);
        map.put(0, zero);
        map.put(1, one);
        map.put(2, two);
        map.put(3, three);
        map.put(4, four);
        map.put(5, five);
        map.put(6, six);
        map.put(7, seven);
        map.put(8, eight);
        map.put(9, nine);
    }

    public void draw(Canvas canvas) {
        if(!collision) {
            ArrayList<Bitmap> digits = convertToBitmaps(score);
            for (int i = 0; i < digits.size(); i++) {
                int x = screenWidth / 2 - digits.size() * zero.getWidth() / 2 + zero.getWidth() * i;
                canvas.drawBitmap(digits.get(i), x, screenHeight/4, null);
            }
        } else {
            ArrayList<Bitmap> currentDigits = convertToBitmaps(score);

            canvas.drawBitmap(bmpScore, screenWidth / 2 - bmpScore.getWidth() / 2, 2 * screenHeight / 4 - zero.getHeight() - bmpScore.getHeight(), null);
            for (int i = 0; i < currentDigits.size(); i++) {
                int x = screenWidth / 2 - currentDigits.size() * zero.getWidth() + zero.getWidth() * i;
                canvas.drawBitmap(currentDigits.get(i), x, screenHeight / 2, null);
            }
        }
    }

    private ArrayList<Bitmap> convertToBitmaps(int amount) {
        ArrayList<Bitmap> digits = new ArrayList();
        if(amount == 0) {
            digits.add(zero);
        }
        while (amount > 0) {
            int lastDigit = amount % 10;
            amount /= 10;
            digits.add(map.get(lastDigit));
        }
        ArrayList<Bitmap> finalDigits = new ArrayList<>();
        for (int i = digits.size()-1; i >= 0; i--) {
            finalDigits.add(digits.get(i));
        }
        return finalDigits;
    }


    public void updateScore() {
        this.score += 1;
    }

    public void collision() {
        collision = true;
    }

    public void saveScore(String userId) {
        httpClient.saveScore(score,userId);
    }
}