package com.map.flappybird.game;

import static android.content.Intent.getIntent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.map.flappybird.component.Background;
import com.map.flappybird.component.Bird;
import com.map.flappybird.component.GameStart;
import com.map.flappybird.component.GameOver;
import com.map.flappybird.component.Obstacle;
import com.map.flappybird.component.Score;

import java.util.ArrayList;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback {

    public GameThread thread;
    private GameState gameState = GameState.INITIAL;
    private String userId;

    private Bird bird;
    private Background background;
    private DisplayMetrics dm;
    private ObstacleManager obstacleManager;
    private GameOver gameOver;
    private GameStart gameStart;
    private Score score;

    public GameManager(Context context, String userId) {
        super(context);
        this.userId = userId;
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        initGame();
    }

    private void initGame() {
        bird = new Bird(getResources(), dm.heightPixels);
        background = new Background(getResources(), dm.heightPixels);
        obstacleManager = new ObstacleManager(getResources(), dm.heightPixels, dm.widthPixels, this);
        gameOver = new GameOver(getResources(), dm.heightPixels, dm.widthPixels);
        gameStart = new GameStart(getResources(), dm.heightPixels, dm.widthPixels);
        score = new Score(getResources(), dm.heightPixels, dm.widthPixels, getContext().getApplicationContext());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        switch (gameState) {
            case PLAYING:
                bird.update();
                obstacleManager.update();
                break;
            case GAME_OVER:
                bird.update();
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(canvas != null) {
            canvas.drawRGB(150, 255, 255);
            background.draw(canvas);
            switch (gameState) {
                case PLAYING:
                    bird.draw(canvas);
                    obstacleManager.draw(canvas);
                    score.draw(canvas);
                    calculateCollision();
                    break;
                case INITIAL:
                    bird.draw(canvas);
                    gameStart.draw(canvas);
                    break;
                case GAME_OVER:
                    bird.draw(canvas);
                    obstacleManager.draw(canvas);
                    gameOver.draw(canvas);
                    score.draw(canvas);
                    break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (gameState) {
            case PLAYING:
                bird.onTouchEvent();
                break;
            case INITIAL:
                bird.onTouchEvent();
                gameState = GameState.PLAYING;
                break;
            case GAME_OVER:
                initGame();
                gameState = GameState.INITIAL;
                break;
        }
        return super.onTouchEvent(event);
    }

    public void obstacleOffscreen() {
        score.updateScore();
    }

    public void calculateCollision() {
        Rect birdPosition = bird.getBirdPosition();
        ArrayList<Obstacle> obstacles = obstacleManager.getObstacles();
        boolean collision = false;
        if(birdPosition.bottom > dm.heightPixels) {
            collision = true;
        } else {
            for(Obstacle obstacle : obstacles) {
                Rect bottomRectangle = obstacle.getPositions().get(0);
                Rect topRectangle = obstacle.getPositions().get(1);
                if(birdPosition.right > bottomRectangle.left && birdPosition.left < bottomRectangle.right && birdPosition.bottom > bottomRectangle.top){
                    collision = true;
                } else if (birdPosition.right > topRectangle.left && birdPosition.left < topRectangle.right && birdPosition.top < topRectangle.bottom) {
                    collision = true;
                }
            }
        }

        if (collision) {
            gameState = GameState.GAME_OVER;
            bird.collision();
            score.collision();
            score.saveScore(userId);
        }
    }
}
