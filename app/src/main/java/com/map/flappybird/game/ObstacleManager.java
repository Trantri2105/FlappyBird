package com.map.flappybird.game;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.map.flappybird.R;
import com.map.flappybird.component.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class ObstacleManager {
    private int interval;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private int screenWidth, screenHeight;
    private Resources resources;
    private int progress = 0;
    private int speed;
    private GameManager gameManager;

    public ObstacleManager(Resources resources, int screenHeight, int screenWidth, GameManager gameManager) {
        this.resources = resources;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        interval = (int) resources.getDimension(R.dimen.obstacle_interval);
        speed = (int) resources.getDimension(R.dimen.obstacle_speed);
        obstacles.add(new Obstacle(resources, screenHeight, screenWidth, this));
        this.gameManager = gameManager;
    }

    public void update() {
        progress += speed;
        if(progress > interval) {
            progress = 0;
            obstacles.add(new Obstacle(resources, screenHeight, screenWidth, this));
        }
        List<Obstacle> duplicate = new ArrayList<>();
        duplicate.addAll(obstacles);
        for (Obstacle obstacle: duplicate) {
            obstacle.update();
        }
    }

    public void draw(Canvas canvas) {
        for (Obstacle obstacle: obstacles) {
            obstacle.draw(canvas);
        }
    }

    public void obstacleOffScreen(Obstacle obstacle) {
        obstacles.remove(obstacle);
        gameManager.obstacleOffscreen();
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }
}
